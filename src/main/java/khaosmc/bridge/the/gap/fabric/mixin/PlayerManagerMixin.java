package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserConnectionEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.UserEventC2SPacket;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	
	@Shadow @Final private MinecraftServer server;
	
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
			String message = getJoinMessage(player).toString();
			UserEvent event = new UserConnectionEvent(user, true, message);
			
			UserEventC2SPacket packet = new UserEventC2SPacket(event);
			ChatBridge.getInstance().sendPacket(packet);
		}
	}
	
	private Text getJoinMessage(ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		String name = profile.getName();
		String cachedName = getCachedPlayerName(profile);
		Text displayName = player.getDisplayName();
		
		TranslatableText joinMessage;
		
		if (cachedName == null || name.equalsIgnoreCase(cachedName)) {
			joinMessage = new TranslatableText("multiplayer.player.joined", displayName);
		} else {
			joinMessage = new TranslatableText("multiplayer.player.joined.renamed", displayName, cachedName);
		}
		
		return joinMessage.formatted(Formatting.YELLOW);
	}
	
	private String getCachedPlayerName(GameProfile profile) {
		UserCache userCache = server.getUserCache();
		GameProfile cachedProfile = userCache.getByUuid(profile.getId());
		
		return (cachedProfile == null) ? profile.getName() : cachedProfile.getName();
	}
}
