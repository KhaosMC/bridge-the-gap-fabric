package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.client.ClientEvent;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ClientEventS2CPacket extends S2CPacket {
	
	public String type;
	public JsonElement event;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		Class<? extends ClientEvent> clazz = Registries.getClazz(Registries.CLIENT_EVENTS, type);
		ClientEvent event = JsonHelper.fromJson(this.event, clazz);
		
		if (event != null) {
			event.handle(source, chatBridge);
		}
	}
}
