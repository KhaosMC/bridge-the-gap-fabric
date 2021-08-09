package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.io.File;
import java.net.URI;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.AuthC2SMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.AuthS2CMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.C2SMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.S2CMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.C2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c.S2CPacket;
import khaosmc.bridge.the.gap.fabric.config.Config;
import khaosmc.bridge.the.gap.fabric.config.ConfigManager;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;

public class ChatBridge {
	
	private static ChatBridge INSTANCE;
	
	public final MinecraftServer mcServer;
	public final BTGClient btgClient;
	public final Config config;
	
	private ChatBridge(MinecraftServer mcServer, BTGClient btgClient, Config config) {
		this.mcServer = mcServer;
		this.btgClient = btgClient;
		this.config = config;
	}
	
	public static ChatBridge getInstance() {
		return INSTANCE;
	}
	
	public static void start(MinecraftServer mcServer) {
		if (!isRunning()) {
			File dir = new File(mcServer.getRunDirectory(), "config");
			Config config = ConfigManager.loadConfig(dir);
			
			if (ConfigManager.isInvalid(config)) {
				BridgeTheGapMod.LOGGER.warn("Please fill in the config file and restart the server!");
			} else {
				URI uri = URI.create(config.server_url);
				BTGClient btgClient = new BTGClient(uri);
				
				INSTANCE = new ChatBridge(mcServer, btgClient, config);
				INSTANCE.onStartup();
			}
		}
	}
	
	public static void stop() {
		if (isRunning()) {
			INSTANCE.onShutdown();
			INSTANCE = null;
		}
	}
	
	public static void restart(MinecraftServer mcServer) {
		if (isRunning()) {
			stop();
		}
		
		start(mcServer);
	}
	
	private static boolean isRunning() {
		return INSTANCE != null;
	}
	
	public static boolean isConnected() {
		return isRunning() && INSTANCE.hasConnection();
	}
	
	private void onStartup() {
		btgClient.connect();
	}
	
	private void onShutdown() {
		btgClient.close();
	}
	
	private boolean hasConnection() {
		return btgClient.isOpen();
	}
	
	public void onServerHandshake() {
		tryAuth();
	}
	
	public void onAuth(AuthS2CMessage auth) {
		String log;
		
		if (auth.success) {
			log = "Successfully authenticated with the websocket server!";
		} else {
			log = "Failed to authenticate with the websocket server: " + auth.reason;
		}
		
		BridgeTheGapMod.LOGGER.info(log);
	}
	
	private void tryAuth() {
		String token = config.auth_token;
		Client client = new Client("minecraft", config.client_name);
		
		AuthC2SMessage auth = new AuthC2SMessage(token, client);
		btgClient.sendMessage(auth);
	}
	
	public void onMessageReceived(S2CMessage message) {
		if (message.source == null || message.payload == null) {
			BridgeTheGapMod.LOGGER.error("Unable to decode packet from websocket server - unknown format");
			return;
		}
		
		Class<? extends S2CPacket> clazz = Registries.getClazz(Registries.S2C_PACKETS, message.type);
		
		if (clazz == null) {
			BridgeTheGapMod.LOGGER.error("Unable to decode message from websocket server - unknown packet type \'" + message.type + "\'");
			return;
		}
		
		S2CPacket packet = JsonHelper.fromJson(message.payload, clazz);
		packet.decode(message.payload);
		
		packet.execute(message.source, this);
	}
	
	public void sendPacket(C2SPacket packet, Client... targets) {
		String type = Registries.getId(Registries.C2S_PACKETS, packet.getClass());
		
		if (type == null) {
			BridgeTheGapMod.LOGGER.error("Unable to encode packet - unknown packet " + packet.getClass());
			return;
		}
		
		JsonElement payload = JsonHelper.toJson(packet);
		
		C2SMessage message = new C2SMessage(type, targets, payload);
		btgClient.sendMessage(message);
	}
	
	public void broadcastChatMessage(Client client, User user, String message) {
		MutableText text = new LiteralText("");
		
		if (client != null) {
			text.append(
				String.format("[%s] ", client.name)
			);
		}
		if (user != null) {
			text.append(
				String.format("<%s> ", user.name)
			);
		}
		text.append(message);
		
		mcServer.getPlayerManager().broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
	}
}
