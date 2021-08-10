package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.AuthC2SMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.AuthS2CMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.C2SMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.S2CMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.C2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.RequestC2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c.S2CPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.request.UserListRequest;
import khaosmc.bridge.the.gap.fabric.config.Config;
import khaosmc.bridge.the.gap.fabric.config.ConfigManager;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class ChatBridge {
	
	private static ChatBridge INSTANCE;
	
	public final MinecraftServer mcServer;
	public final BTGClient btgClient;
	public final Config config;
	
	private final Map<String, Client> clients;
	private final Map<String, Map<String, User>> users;
	
	private ChatBridge(MinecraftServer mcServer, BTGClient btgClient, Config config) {
		this.mcServer = mcServer;
		this.btgClient = btgClient;
		this.config = config;
		
		this.clients = new HashMap<>();
		this.users = new HashMap<>();
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
		
		if (auth.success) {
			UserListRequest request = new UserListRequest();
			RequestC2SPacket packet = new RequestC2SPacket(request);
			sendPacket(packet);
		}
	}
	
	private void tryAuth() {
		String token = config.auth_token;
		Client client = new Client("minecraft", config.client_name, config.client_display_color);
		
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
		
		JsonHelper.fromJson(message.payload, clazz).execute(message.source, this);
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
		broadcastChatMessage(client, user, new LiteralText(message));
	}
	
	public void broadcastChatMessage(Client client, User user, Text message) {
		MutableText text = new LiteralText("");
		
		if (client != null) {
			text.
				append(TextHelper.fancyFormatClientName(client)).
				append(" ");
		}
		if (user != null) {
			text.
				append(TextHelper.fancyFormatUserName(client, user)).
				append(" ");
		}
		text.append(message);
		
		mcServer.getPlayerManager().broadcastChatMessage(text, MessageType.CHAT, Util.NIL_UUID);
	}
	
	public void broadcastWhisper(Client fromClient, User fromUser, User toUser, String message) {
		MutableText text = new LiteralText("").formatted(Formatting.GRAY, Formatting.ITALIC);
		
		text.
			append(TextHelper.fancyFormatClientName(fromClient)).
			append(" ");
		if (fromUser != null) {
			text.
				append(TextHelper.fancyFormatUserName(fromClient, fromUser)).
				append(" ");
		}
		text.
			append(new LiteralText(String.format("whispers to you: %s", message)));
		
		if (toUser == null) {
			mcServer.getPlayerManager().broadcastChatMessage(text, MessageType.SYSTEM, Util.NIL_UUID);
		} else {
			ServerPlayerEntity toPlayer = mcServer.getPlayerManager().getPlayer(toUser.name);
			
			if (toPlayer != null) {
				toPlayer.sendSystemMessage(text, Util.NIL_UUID);
			}
		}
	}
	
	public void updateUserList(Client client, User[] userList) {
		clients.put(client.name, client);
		Map<String, User> userMap = users.computeIfAbsent(client.name, clientName -> new HashMap<>());
		
		for (User user : userList) {
			userMap.put(user.name, user);
		}
	}
	
	public Collection<String> getClientNames() {
		return clients.keySet();
	}
	
	public Collection<Client> getClients() {
		return clients.values();
	}
	
	public Client getClient(String name) {
		return clients.get(name);
	}
	
	public Collection<String> getUserNames(String clientName) {
		Map<String, User> userMap = users.get(clientName);
		return userMap == null ? Collections.emptyList() : userMap.keySet();
	}
	
	public Collection<User> getUsers(String clientName) {
		Map<String, User> userMap = users.get(clientName);
		return userMap == null ? Collections.emptyList() : userMap.values();
	}
	
	public User getUser(String clientName, String userName) {
		Map<String, User> userMap = users.get(clientName);
		return userMap == null ? null : userMap.get(userName);
	}
	
	public void onClientConnect(Client client) {
		clients.put(client.name, client);
		users.computeIfAbsent(client.name, clientName -> new HashMap<>());
	}
	
	public void onClientDisconnect(Client client) {
		clients.remove(client.name);
		users.remove(client.name);
	}
	
	public void onUserConnect(Client client, User user) {
		onClientConnect(client);
		users.get(client.name).put(user.name, user);
	}
	
	public void onUserDisconnect(Client client, User user) {
		onClientConnect(client);
		users.get(client.name).remove(user.name);
	}
}
