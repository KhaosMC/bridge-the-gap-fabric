package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserConnectionEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.ChatMessageC2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.UserEventC2SPacket;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	
	@Shadow @Final private MinecraftServer server;
	@Shadow private ServerPlayerEntity player;
	
	@Inject(
			method = "method_31286",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"
			)
	)
	private void onChatMessage(String message, CallbackInfo ci) {
		if (ChatBridge.isConnected()) {
			ChatMessageC2SPacket packet = new ChatMessageC2SPacket(User.fromPlayer(player), message);
			ChatBridge.getInstance().sendPacket(packet);
		}
	}
	
	@Inject(
			method = "onDisconnected",
			at = @At(
					value = "HEAD"
			)
	)
	private void onPlayerJoin(Text reason, CallbackInfo ci) {
		if (ChatBridge.isConnected()) {
			String message = getLeaveMessage().getString();
			UserEvent event = new UserConnectionEvent(false, message);
			
			User user = User.fromPlayer(player);
			UserEventC2SPacket packet = new UserEventC2SPacket(user, event);
			ChatBridge.getInstance().sendPacket(packet);
		}
	}
	
	private Text getLeaveMessage() {
		return new TranslatableText("multiplayer.player.left", player.getDisplayName()).formatted(Formatting.YELLOW);
	}
}
