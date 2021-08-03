package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class ChatMessageC2SPacket extends C2SPacket {
	
	public User user;
	public String content;
	
	public ChatMessageC2SPacket(User author, String content) {
		this.user = author;
		this.content = content;
	}
}
