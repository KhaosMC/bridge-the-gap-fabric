package khaosmc.bridge.the.gap.fabric.chatbridge.message;

import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class AuthC2SMessage extends Message {
	
	public String token;
	public Client client;
	
	public AuthC2SMessage(String token, Client client) {
		this.type = "auth";
		this.token = token;
		this.client = client;
	}
}
