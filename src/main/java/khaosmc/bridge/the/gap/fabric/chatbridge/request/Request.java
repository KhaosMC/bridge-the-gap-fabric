package khaosmc.bridge.the.gap.fabric.chatbridge.request;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class Request implements JsonSerializable {
	
	public static void registerRequests(Registry<Request> registry) {
		
	}
	
	public abstract void handle(Client client, ChatBridge chatBridge);
	
}
