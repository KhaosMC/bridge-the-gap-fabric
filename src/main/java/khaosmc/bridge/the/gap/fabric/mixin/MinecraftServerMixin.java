package khaosmc.bridge.the.gap.fabric.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z"
			)
	)
	private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (!ChatBridge.isConnected()) {
			ChatBridge.restart((MinecraftServer)(Object)this);
		}
	}
}
