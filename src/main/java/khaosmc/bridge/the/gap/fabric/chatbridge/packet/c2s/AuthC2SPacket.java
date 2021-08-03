package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class AuthC2SPacket extends C2SPacket {
	
	public String token;
	public Client client;
	
	public AuthC2SPacket(String token, Client client) {
		this.token = token;
		this.client = client;
	}
}
