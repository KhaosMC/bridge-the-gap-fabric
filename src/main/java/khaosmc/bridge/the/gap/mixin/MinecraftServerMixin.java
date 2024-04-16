package khaosmc.bridge.the.gap.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z"
		)
	)
	private void btg$tryRestartChatBridgeOnAutoSave(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
		if (!BridgeTheGapClient.isConnected()) {
			BridgeTheGapClient.restart((MinecraftServer)(Object)this);
		}
	}
}
