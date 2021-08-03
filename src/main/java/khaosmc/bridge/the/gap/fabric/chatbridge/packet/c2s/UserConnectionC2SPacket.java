package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class UserConnectionC2SPacket extends C2SPacket {
	
	public User user;
	public boolean connect;
	public String message;
	
	public UserConnectionC2SPacket(User user, boolean connect, String message) {
		this.user = user;
		this.connect = connect;
		this.message = message;
	}
	
	public UserConnectionC2SPacket(User user, boolean connect) {
		this(user, connect, "");
	}
}
