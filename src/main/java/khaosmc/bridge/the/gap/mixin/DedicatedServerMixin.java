package khaosmc.bridge.the.gap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

	@Inject(
		method = "initServer",
		at = @At(
			value = "RETURN"
		)
	)
	private void btg$init(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			BridgeTheGapClient.start((MinecraftServer)(Object)this);
		}
	}

	@Inject(
		method = "stopServer",
		at = @At(
			value = "TAIL"
		)
	)
	private void btg$stop(CallbackInfo ci) {
		BridgeTheGapClient.stop();
	}
}
