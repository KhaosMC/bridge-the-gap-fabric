package khaosmc.bridge.the.gap.fabric.chatbridge.event.client;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class ClientEvent {
	
	public static void registerEvents(Registry<ClientEvent> registry) {
		Registries.register(registry, "connection", ClientConnectionEvent.class);
	}
	
	public abstract void handle(Client client, ChatBridge chatBridge);
	
}
