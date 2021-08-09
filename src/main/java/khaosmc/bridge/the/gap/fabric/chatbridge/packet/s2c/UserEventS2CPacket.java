package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class UserEventS2CPacket extends S2CPacket {
	
	public String type;
	public User user;
	public UserEvent event;
	
	@Override
	public void decode(JsonElement rawJson) {
		event = null; // overwrite the empty UserEvent that Gson writes
		
		JsonObject packetJson = rawJson.getAsJsonObject();
		JsonElement rawEventJson = packetJson.get("event");
		
		Class<? extends UserEvent> clazz = Registries.getClazz(Registries.USER_EVENTS, type);
		event = JsonHelper.fromJson(rawEventJson, clazz);
	}
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		if (event != null) {
			event.handle(source, user, chatBridge);
		}
	}
}
