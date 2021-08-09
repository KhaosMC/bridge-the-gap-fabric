package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ResponseS2CPacket extends S2CPacket {
	
	public String type;
	public Response response;
	
	@Override
	public void decode(JsonElement rawJson) {
		response = null; // overwrite the empty Response Gson writes
		
		JsonObject packetJson = rawJson.getAsJsonObject();
		JsonElement requestJson = packetJson.get("response");
		
		Class<? extends Response> clazz = Registries.getClazz(Registries.RESPONSES, type);
		response = JsonHelper.fromJson(requestJson, clazz);
	}
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		if (response != null) {
			response.handle(source, chatBridge);
		}
	}
}
