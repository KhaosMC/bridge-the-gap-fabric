package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.UserConnectionC2SPacket;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	
	@Inject(
			method = "onPlayerConnect",
			at = @At(
					value = "HEAD"
			)
	)
	private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		if (ChatBridge.isConnected()) {
			User user = User.fromPlayer(player);
			String message = String.format("%s joined the game", player.getEntityName());
			
			UserConnectionC2SPacket packet = new UserConnectionC2SPacket(user, true, message);
			ChatBridge.getInstance().sendPacket(packet);
		}
	}
}
