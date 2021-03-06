package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class RequestS2CPacket extends S2CPacket {
	
	public String type;
	public JsonElement request;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		Class<? extends Request> clazz = Registries.getClazz(Registries.REQUESTS, type);
		Request request = JsonHelper.fromJson(this.request, clazz);
		
		if (request != null) {
			request.handle(source, chatBridge);
		}
	}
}
