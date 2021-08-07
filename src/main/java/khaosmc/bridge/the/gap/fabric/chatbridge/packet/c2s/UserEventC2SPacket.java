package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;

public class UserEventC2SPacket extends C2SPacket {
	
	public UserEvent user_event;
	
	public UserEventC2SPacket(UserEvent user_event) {
		this.user_event = user_event;
	}
}
