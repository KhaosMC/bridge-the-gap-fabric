package khaosmc.bridge.the.gap.fabric.chatbridge.event.client;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class ClientConnectionEvent extends ClientEvent {
	
	public boolean connect;
	public String message;
	
	public ClientConnectionEvent(boolean connect, String message) {
		this.connect = connect;
		this.message = message;
	}
	
	public ClientConnectionEvent(boolean connect) {
		this(connect, "");
	}
	
	@Override
	public void handle(Client client, ChatBridge chatBridge) {
		String m = message;
		
		if (m.isEmpty()) {
			m = String.format("%s client %s %s to the chat bridge!", client.type, client.name, connect ? "connected" : "disconnected");
		}
		
		chatBridge.broadcastChatMessage(null, null, m);
	}
}
