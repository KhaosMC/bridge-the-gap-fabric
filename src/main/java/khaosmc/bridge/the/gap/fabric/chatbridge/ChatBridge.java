package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.net.URI;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.Message;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.MessageBuilder;
import khaosmc.bridge.the.gap.fabric.config.Config;
import khaosmc.bridge.the.gap.fabric.config.ConfigManager;

import net.minecraft.network.MessageType;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class ChatBridge {
	
	public static ChatBridge INSTANCE;
	
	private final MinecraftServer mcServer;
	private final BTGClient btgClient;
	private final Config config;
	
	private ChatBridge(MinecraftServer mcServer, BTGClient btgClient, Config config) {
		this.mcServer = mcServer;
		this.btgClient = btgClient;
		this.config = config;
	}
	
	public static void start(MinecraftServer mcServer) {
		if (isRunning()) {
			BridgeTheGapMod.LOGGER.warn("Cannot start the chat bridge as it is already running!");
		} else {
			Config config = ConfigManager.loadConfig(mcServer.getRunDirectory());
			
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
	
	public static boolean isConnected() {
		return isRunning() && INSTANCE.hasConnection();
	}
	
	private static boolean isRunning() {
		return INSTANCE != null;
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
	
	public void onOpen() {
		Message message = new MessageBuilder().
				setType("auth").
				setClient(config.client_type, config.client_name).
				setContent(config.auth_token).
				build();
		btgClient.sendMessage(message);
	}
	
	public void onMessageReceived(Message message) {
		Text text = new LiteralText(
				String.format("[%s] ", message.client.name)
			).append(new LiteralText(
				String.format("<%s> ", message.author.name)
			)).append(message.content);
		mcServer.getPlayerManager().broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
	}
	
	public void sendChatMessage(ServerPlayerEntity player, String content) {
		String name = player.getEntityName();
		int displayColor = 0xFFFFFF;
		
		AbstractTeam team = player.getScoreboardTeam();
		
		if (team != null) {
			Integer color = team.getColor().getColorValue();
			
			if (color != null) {
				displayColor = color;
			}
		}
		
		Message message = new MessageBuilder().
				setType("chat").
				setClient(config.client_type, config.client_name).
				setAuthor(name, displayColor).
				setContent(content).
				build();
		btgClient.sendMessage(message);
	}
}
