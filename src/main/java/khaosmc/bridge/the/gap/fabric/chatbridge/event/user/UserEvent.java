package khaosmc.bridge.the.gap.fabric.chatbridge.event.user;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public abstract class UserEvent implements JsonSerializable {
	
	static {
		
		Registries.register(Registries.USER_EVENTS, "connection", UserConnectionEvent.class);
		
	}
	
	public User user;
	
	protected UserEvent(User user) {
		this.user = user;
	}
	
	public abstract void execute(Client client, ChatBridge chatBridge);
	
}
