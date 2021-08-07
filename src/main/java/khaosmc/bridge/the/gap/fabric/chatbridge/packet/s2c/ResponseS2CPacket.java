package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ResponseS2CPacket extends S2CPacket {
	
	public Response response;
	
	@Override
	public void decode(Gson gson, JsonObject packetData) {
		JsonElement requestJson = packetData.get("response");
		response = JsonHelper.fromJson(requestJson, Registries.RESPONSES);
	}
	
	@Override
	public void execute(ChatBridge chatBridge) {
		if (response != null) {
			response.handle(source, chatBridge);
		}
	}
}
