package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.client.ClientEvent;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ClientEventS2CPacket extends S2CPacket {
	
public ClientEvent client_event;
	
	@Override
	public void decode(Gson gson, JsonObject packetData) {
		JsonElement eventJson = packetData.get("user_event");
		client_event = JsonHelper.fromJson(eventJson, Registries.CLIENT_EVENTS);
	}
	
	@Override
	public void execute(ChatBridge chatBridge) {
		if (client_event != null) {
			client_event.execute(source, chatBridge);
		}
	}
}
