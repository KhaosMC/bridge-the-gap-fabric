package khaosmc.bridge.the.gap.fabric.chatbridge.request;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;

public abstract class Request implements JsonSerializable {
	
	static {
		
		
		
	}
	
	public abstract void handle(Client client, ChatBridge chatBridge);
	
}
