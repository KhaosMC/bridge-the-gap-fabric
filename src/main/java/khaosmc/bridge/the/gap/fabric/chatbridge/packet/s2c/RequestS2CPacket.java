package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class RequestS2CPacket extends S2CPacket {
	
	public Request request;
	
	@Override
	public void decode(Gson gson, JsonObject packetData) {
		JsonElement requestJson = packetData.get("request");
		request = JsonHelper.fromJson(requestJson, Registries.REQUESTS);
	}
	
	@Override
	public void execute(ChatBridge chatBridge) {
		if (request != null) {
			request.handle(source, chatBridge);
		}
	}
}
