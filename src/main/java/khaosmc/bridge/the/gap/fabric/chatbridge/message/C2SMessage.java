package khaosmc.bridge.the.gap.fabric.chatbridge.message;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class C2SMessage extends Message {
	
	public Client[] targets;
	public JsonElement payload;
	
	public C2SMessage(String type, Client[] targets, JsonElement payload) {
		this.type = type;
		this.targets = targets;
		this.payload = payload;
	}
}
