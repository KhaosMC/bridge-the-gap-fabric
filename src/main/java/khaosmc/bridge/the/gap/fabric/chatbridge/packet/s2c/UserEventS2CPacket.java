package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class UserEventS2CPacket extends S2CPacket {
	
	public UserEvent user_event;
	
	@Override
	public void decode(Gson gson, JsonObject packetData) {
		JsonElement eventJson = packetData.get("user_event");
		user_event = JsonHelper.fromJson(eventJson, Registries.USER_EVENTS);
	}
	
	@Override
	public void execute(ChatBridge chatBridge) {
		if (user_event != null) {
			user_event.execute(source, chatBridge);
		}
	}
}
