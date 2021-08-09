package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class RequestS2CPacket extends S2CPacket {
	
	public String type;
	public Request request;
	
	@Override
	public void decode(JsonElement rawJson) {
		request = null; // overwrite the empty Request Gson writes
		
		JsonObject packetJson = rawJson.getAsJsonObject();
		JsonElement requestJson = packetJson.get("request");
		
		Class<? extends Request> clazz = Registries.getClazz(Registries.REQUESTS, type);
		request = JsonHelper.fromJson(requestJson, clazz);
	}
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		if (request != null) {
			request.handle(source, chatBridge);
		}
	}
}
