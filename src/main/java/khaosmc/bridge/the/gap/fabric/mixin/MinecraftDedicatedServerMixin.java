package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {
	
	@Inject(
			method = "setupServer",
			at = @At(
					value = "RETURN"
			)
	)
	private void onSetupServer(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			ChatBridge.start((MinecraftServer)(Object)this);
		}
	}
	
	@Inject(
			method = "exit",
			at = @At(
					value = "HEAD"
			)
	)
	private void onExit(CallbackInfo ci) {
		ChatBridge.stop();
	}
}
