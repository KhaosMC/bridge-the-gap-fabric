package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ResponseS2CPacket extends S2CPacket {
	
	public String type;
	public JsonElement response;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		Class<? extends Response> clazz = Registries.getClazz(Registries.RESPONSES, type);
		Response response = JsonHelper.fromJson(this.response, clazz);
		
		if (response != null) {
			response.handle(source, chatBridge);
		}
	}
}
