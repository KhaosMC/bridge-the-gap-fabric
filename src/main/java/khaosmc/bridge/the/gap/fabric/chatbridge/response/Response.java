package khaosmc.bridge.the.gap.fabric.chatbridge.response;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class Response {
	
	public static void registerResponses(Registry<Response> registry) {
		Registries.register(registry, "user_list", UserListResponse.class);
	}
	
	public abstract void handle(Client client, ChatBridge chatBridge);
	
}
