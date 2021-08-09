package khaosmc.bridge.the.gap.fabric.chatbridge.event.user;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class UserConnectionEvent extends UserEvent {
	
	public boolean connect;
	public String message;
	
	public UserConnectionEvent(boolean connect, String message) {
		this.connect = connect;
		this.message = message;
	}
	
	@Override
	public void handle(Client client, User user, ChatBridge chatBridge) {
		String m = message;
		
		if (m.isEmpty()) {
			m = String.format("%s %s!", user.name, connect ? "connected" : "disconnected");
		}
		
		chatBridge.broadcastChatMessage(client, null, m);
	}
}
