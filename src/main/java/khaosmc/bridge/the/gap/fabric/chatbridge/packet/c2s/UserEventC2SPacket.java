package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class UserEventC2SPacket extends C2SPacket {
	
	public String type;
	public User user;
	public UserEvent event;
	
	public UserEventC2SPacket(User user, UserEvent event) {
		this.type = Registries.getId(Registries.USER_EVENTS, event.getClass());
		this.user = user;
		this.event = event;
	}
}
