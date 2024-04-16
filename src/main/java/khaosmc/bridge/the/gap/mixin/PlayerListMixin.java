package khaosmc.bridge.the.gap.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundUserConnectPayload;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

@Mixin(PlayerList.class)
public class PlayerListMixin {

	@Inject(
		method = "placeNewPlayer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
		)
	)
	private void btg$sendPlayerJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
		if (BridgeTheGapClient.isConnected()) {
			ServerBoundUserConnectPayload packet = new ServerBoundUserConnectPayload(player);
			BridgeTheGapClient.getInstance().sendPacket(packet);
		}
	}

	private static final Set<String> IGNORED_KEYS = Util.make(new HashSet<>(), set -> {
		set.add("multiplayer.player.joined");
		set.add("multiplayer.player.joined.renamed");
		set.add("multiplayer.player.left");
	});

	@Inject(
		method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void btg$sendSystemMessage(Component message, boolean actionBar, CallbackInfo ci) {
		if (!actionBar) {
			if (message.getContents() instanceof TranslatableContents contents) {
				String key = contents.getKey();

				for (String ignoredKey : IGNORED_KEYS) {
					if (key.equals(ignoredKey)) {
						return;
					}
				}
			}

			BridgeTheGapClient.getInstance().sendChatMessage(null, message.getString());
		}
	}

	@Inject(
		method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/network/chat/ChatType$Bound;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void btg$sendChatMessage(PlayerChatMessage message, CommandSourceStack source, ChatType.Bound bound, CallbackInfo ci) {
		BridgeTheGapClient.getInstance().sendChatMessage(source.getPlayer(), message.signedContent());
	}

	@Inject(
		method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
		at = @At(
			value = "HEAD"
		)
	)
	private void btg$sendChatMessage(PlayerChatMessage message, ServerPlayer source, ChatType.Bound bound, CallbackInfo ci) {
		BridgeTheGapClient.getInstance().sendChatMessage(source, message.signedContent());
	}
}
