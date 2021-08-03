package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.io.File;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.Packets;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.AuthC2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.C2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c.S2CPacket;
import khaosmc.bridge.the.gap.fabric.config.Config;
import khaosmc.bridge.the.gap.fabric.config.ConfigManager;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;

public class ChatBridge {
	
	private static final Gson GSON = new Gson();
	
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
	
	public void tryAuth() {
		String token = config.auth_token;
		Client client = new Client("minecraft", config.client_name);
		AuthC2SPacket packet = new AuthC2SPacket(token, client);
		
		sendPacket(packet);
	}
	
	public void onPacketReceived(String rawPacket) {
		JsonElement rawJson = GSON.fromJson(rawPacket, JsonElement.class);
		
		if (!rawJson.isJsonObject()) {
			BridgeTheGapMod.LOGGER.error("Unable to decode packet - unknown format");
			return;
		}
		
		JsonObject json = rawJson.getAsJsonObject();
		
		if (!json.has("type")) {
			BridgeTheGapMod.LOGGER.error("Unable to decode packet - unknown format");
			return;
		}
		
		String type = json.get("type").getAsString();
		Class<? extends S2CPacket> clazz = Packets.getClazzS2C(type);
		
		if (clazz == null) {
			BridgeTheGapMod.LOGGER.error("Unable to decode packet - unknown type \'" + type + "\'");
			return;
		}
		
		GSON.fromJson(json, clazz).execute(this);
	}
	
	public void sendPacket(C2SPacket packet) {
		String type = Packets.getTypeC2S(packet);
		
		if (type == null) {
			BridgeTheGapMod.LOGGER.error("Unable to encode packet - unknown type " + packet.getClass());
			return;
		}
		
		JsonElement rawJson = GSON.toJsonTree(packet);
		JsonObject json = rawJson.getAsJsonObject();
		json.addProperty("type", type);
		
		btgClient.send(json.toString());
	}
	
	public void broadcastChatMessage(Client client, User user, String message) {
		MutableText text = new LiteralText(
			String.format("[%s] ", client.name)
		);
		if (user != null) {
			text.append(
				String.format("<%s> ", user.name)
			);
		}
		text.append(message);
		
		mcServer.getPlayerManager().broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
	}
}
