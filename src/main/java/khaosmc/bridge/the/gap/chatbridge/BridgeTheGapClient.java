package khaosmc.bridge.the.gap.chatbridge;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import khaosmc.bridge.the.gap.BridgeTheGapMod;
import khaosmc.bridge.the.gap.chat.Components;
import khaosmc.bridge.the.gap.chatbridge.packet.Payloads;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundClientConnectPayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundClientMessagePayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundPayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundUserConnectPayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundUserMessagePayload;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class BridgeTheGapClient extends WebSocketClient {

	private static final String PROTOCOL = "1";

	private static BridgeTheGapClient instance;

	public static BridgeTheGapClient getInstance() {
		return instance;
	}

	public static void start(MinecraftServer server) {
		if (isRunning()) {
			throw new IllegalStateException("chat bridge is already running!");
		} else {
			File dir = server.getFile("config");
			Config config = Config.load(dir.toPath());

			instance = new BridgeTheGapClient(server, config);
			instance.connect();
		}
	}

	public static void stop() {
		if (isRunning()) {
			instance.close();
		} else {
			throw new IllegalStateException("chat bridge is not running!");
		}
	}

	public static void restart(MinecraftServer server) {
		if (isRunning()) {
			stop();
		}

		start(server);
	}

	private static boolean isRunning() {
		return instance != null;
	}

	public static boolean isConnected() {
		return isRunning() && instance.isOpen();
	}

	private final MinecraftServer mcServer;
	private final Config config;
	private final PacketHandler listener;

	public BridgeTheGapClient(MinecraftServer server, Config config) {
		super(URI.create(config.server_url()));

		this.mcServer = server;
		this.config = config;
		this.listener = new PacketHandler(this, mcServer);

		addHeader("protocol", PROTOCOL);
		addHeader("token", config.auth_token());
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		if (PROTOCOL.equals(handshakedata.getFieldValue("protocol"))) {
			sendPacket(new ServerBoundClientConnectPayload());
			// TODO: something better than this hack
			for (ServerPlayer player : mcServer.getPlayerList().getPlayers()) {
				sendPacket(new ServerBoundUserConnectPayload(player));
			}
			updatePermissions();
		} else {
			close(69, "incompatible protocol!");
		}

		BridgeTheGapMod.LOGGER.info(handshakedata.getHttpStatusMessage());
	}

	@Override
	public void onMessage(String message) {
		try {
			Payloads.deserialize(message).handle(listener);
		} catch (IOException e) {
			throw new RuntimeException("error handling packet", e);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (code != 69) {
			updatePermissions();
		}

		BridgeTheGapMod.LOGGER.info("chat bridge closed: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		BridgeTheGapMod.LOGGER.error("chat bridge encountered an error", ex);
	}

	public String getName() {
		return config.client_name();
	}

	public int getColor() {
		return config.client_display_color();
	}

	private void updatePermissions() {
		for (ServerPlayer player : mcServer.getPlayerList().getPlayers()) {
			mcServer.getPlayerList().sendPlayerPermissionLevel(player);
		}
	}

	public void sendPacket(ServerBoundPayload packet) {
		try {
			send(Payloads.serialize(packet));
		} catch (IOException e) {
			throw new RuntimeException("error sending packet", e);
		}
	}

	public void sendChatMessage(ServerPlayer source, String message) {
		if (source == null) {
			sendPacket(new ServerBoundClientMessagePayload(message));
		} else {
			sendPacket(new ServerBoundUserMessagePayload(source, message));
		}
	}

	public void clientConnected(String name, int color) {
		mcServer.getPlayerList().broadcastSystemMessage(
			Component
				.literal("")
				.append(Components.clientDisplayName(name, color))
				.append(" connected to the chat bridge")
				.withStyle(ChatFormatting.YELLOW),
			false
		);
	}

	public void clientDisconnected(String name, int color) {
		mcServer.getPlayerList().broadcastSystemMessage(
			Component
				.literal("")
				.append(Components.clientDisplayName(name, color))
				.append(" discconnected from the chat bridge")
				.withStyle(ChatFormatting.YELLOW),
			false
		);
	}

	public void userConnected(String clientName, int clientColor, String userId, String userName, int userColor) {
		mcServer.getPlayerList().broadcastSystemMessage(
			Component
				.literal("")
				.append(Components.clientDecoratedName(clientName, clientColor))
				.append(" ")
				.append(Components.userDisplayName(clientName, userId, userName, userColor))
				.append(" joined")
				.withStyle(ChatFormatting.YELLOW),
			false
		);
	}

	public void userDisconnected(String clientName, int clientColor, String userId, String userName, int userColor) {
		mcServer.getPlayerList().broadcastSystemMessage(
			Component
				.literal("")
				.append(Components.clientDecoratedName(clientName, clientColor))
				.append(" ")
				.append(Components.userDisplayName(clientName, userId, userName, userColor))
				.append(" left")
				.withStyle(ChatFormatting.YELLOW),
			false
		);
	}
}
