package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class WhisperC2SPacket extends C2SPacket {
	
	public User fromUser;
	public User toUser;
	public String message;
	
	public WhisperC2SPacket(User fromUser, User toUser, String message) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.message = message;
	}
}
