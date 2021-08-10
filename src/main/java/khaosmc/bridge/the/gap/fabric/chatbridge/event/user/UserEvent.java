package khaosmc.bridge.the.gap.fabric.chatbridge.event.user;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class UserEvent {
	
	public static void registerEvents(Registry<UserEvent> registry) {
		Registries.register(registry, "connection", UserConnectionEvent.class);
	}
	
	public abstract void handle(Client client, User user, ChatBridge chatBridge);
	
}
