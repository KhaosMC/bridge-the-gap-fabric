package khaosmc.bridge.the.gap.fabric.chatbridge.response;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class Response implements JsonSerializable {
	
public static void registerResponses(Registry<Response> registry) {
		
	}
	
	public abstract void handle(Client client, ChatBridge chatBridge);
	
}
