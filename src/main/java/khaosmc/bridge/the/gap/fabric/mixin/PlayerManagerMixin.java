package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserConnectionEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.UserEventC2SPacket;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	
	@Inject(
			method = "onPlayerConnect",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/MinecraftServer;forcePlayerSampleUpdate()V"
					)
			),
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"
			)
	)
	private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		if (ChatBridge.isConnected()) {
			User user = User.fromPlayer(player);
			UserEvent event = new UserConnectionEvent(true);
			UserEventC2SPacket packet = new UserEventC2SPacket(user, event);
			ChatBridge.getInstance().sendPacket(packet);
		}
	}
}
