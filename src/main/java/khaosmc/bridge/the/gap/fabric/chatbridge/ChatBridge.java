package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.io.File;
import java.net.URI;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.AuthC2SPacket;
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
	
	public void tryAuth() {
		String token = config.auth_token;
		Client client = new Client("minecraft", config.client_name);
		AuthC2SPacket packet = new AuthC2SPacket(token, client);
		
		sendPacket(packet);
	}
	
	public void onPacketReceived(String rawPacket) {
		S2CPacket packet = JsonHelper.fromJson(rawPacket, Registries.S2C_PACKETS);
		
		if (packet != null) {
			packet.execute(this);
		}
	}
	
	public void sendPacket(C2SPacket packet) {
		String rawPacket = JsonHelper.toJson(packet, Registries.C2S_PACKETS);
		
		if (rawPacket != null) {
			btgClient.send(rawPacket);
		}
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
