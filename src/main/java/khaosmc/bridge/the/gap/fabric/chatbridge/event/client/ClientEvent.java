package khaosmc.bridge.the.gap.fabric.chatbridge.event.client;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public abstract class ClientEvent implements JsonSerializable {
	
	static {
		
		Registries.register(Registries.CLIENT_EVENTS, "connection", ClientConnectionEvent.class);
		
	}
	
	public abstract void execute(Client client, ChatBridge chatBridge);
	
}
