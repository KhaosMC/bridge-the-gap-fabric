package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class ChatMessageC2SPacket extends C2SPacket {
	
	public User user;
	public String message;
	
	public ChatMessageC2SPacket(User author, String message) {
		this.user = author;
		this.message = message;
	}
}
