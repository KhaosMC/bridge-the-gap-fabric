package khaosmc.bridge.the.gap.chatbridge;

import khaosmc.bridge.the.gap.BridgeTheGapMod;
import khaosmc.bridge.the.gap.chat.Components;
import khaosmc.bridge.the.gap.chatbridge.packet.clientbound.*;
import khaosmc.bridge.the.gap.chatbridge.packet.request.Request;
import khaosmc.bridge.the.gap.chatbridge.packet.response.Response;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.*;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PacketHandler {

	private final BridgeTheGapClient chatBridge;
	private final MinecraftServer server;
	private final RequestHandler requestHandler;

	public PacketHandler(BridgeTheGapClient chatBridge, MinecraftServer server) {
		this.chatBridge = chatBridge;
		this.server = server;
		this.requestHandler = new RequestHandler(chatBridge, server, this);
	}

	public void handleClientConnect(ClientBoundClientConnectPayload packet) {
		chatBridge.clientConnected(packet.name(), packet.color());
	}

	public void handleClientDisconnect(ClientBoundClientDisconnectPayload packet) {
		chatBridge.clientDisconnected(packet.name(), packet.color());
	}

	public void handleUserConnect(ClientBoundUserConnectPayload packet) {
		chatBridge.userConnected(packet.clientName(), packet.clientColor(), packet.userId(), packet.userName(), packet.userColor());
	}

	public void handleUserDisconnect(ClientBoundUserDisconnectPayload packet) {
		chatBridge.userDisconnected(packet.clientName(), packet.clientColor(), packet.userId(), packet.userName(), packet.userColor());
	}

	public void handleClientMessage(ClientBoundClientMessagePayload packet) {
		sendChatMessage(packet.clientName(), packet.clientColor(), null, null, -1, packet.message());
	}

	public void handleUserMessage(ClientBoundUserMessagePayload packet) {
		sendChatMessage(packet.clientName(), packet.clientColor(), packet.userId(), packet.userName(), packet.userColor(), packet.message());
	}

	private void sendChatMessage(String clientName, int clientColor, String userId, String userName, int userColor, String m) {
		MutableComponent message = Component.literal("")
			.append(Components.clientDecoratedName(clientName, clientColor))
			.append(" ");
		if (userId != null) {
			message
				.append(Components.userDecoratedName(clientName, userId, userName, userColor))
				.append(" ");
		}
		message.append(m);

		server.getPlayerList().broadcastSystemMessage(message, false);
	}

	public void handleClientWhisper(ClientBoundClientWhisperPayload packet) {
		whisper(packet.clientName(), packet.clientColor(), null, null, -1, null, packet.message());
	}

	public void handleUserWhisper(ClientBoundUserWhisperPayload packet) {
		whisper(packet.clientName(), packet.clientColor(), packet.userId(), packet.userName(), packet.userColor(), packet.targetUser(), packet.message());
	}

	private void whisper(String clientName, int clientColor, String userId, String userName, int userColor, String targetId, String m) {
		ServerPlayer target = server.getPlayerList().getPlayerByName(targetId);

		MutableComponent message = Component.literal("")
			.append(Components.clientDisplayName(clientName, clientColor))
			.withStyle(ChatFormatting.ITALIC);
		if (userId != null) {
			message
				.append(
					Components.userDisplayName(clientName, userId, userName, userColor))	
				.withStyle(ChatFormatting.ITALIC);
		}
		message
			.append(Component
				.literal(" whispers to you: ")
				.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
			.append(Component
				.literal(m)
				.withStyle(ChatFormatting.ITALIC));

		if (target == null) {
			server.sendSystemMessage(message);
		} else {
			target.sendSystemMessage(message);
		}
	}

	public void handleRequest(ClientBoundRequestPayload packet) {
		packet.data().handle(requestHandler);
	}

	public void handleResponseDenied(ClientBoundResponseDeniedPayload packet) {
		BridgeTheGapMod.LOGGER.info("request " + packet.transaction() + " was denied due to: " + packet.reason());
	}

	public void handleResponseGranted(ClientBoundResponseGrantedPayload packet) {
		packet.data().handle(requestHandler);
	}

	public void sendRequest(String client, int transaction, Request request) {
		chatBridge.sendPacket(new ServerBoundRequestPayload(client, transaction, request));
	}

	public void denyResponse(String client, int transaction, String reason) {
		chatBridge.sendPacket(new ServerBoundResponseDeniedPayload(client, transaction, reason));
	}

	public void grantResponse(String client, int transaction, Response response) {
		chatBridge.sendPacket(new ServerBoundResponseGrantedPayload(client, transaction, response));
	}
}
