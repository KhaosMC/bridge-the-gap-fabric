package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class UserConnectionS2CPacket extends S2CPacket {
	
	public User user;
	public boolean connect;
	public String message;
	
	@Override
	public void execute(ChatBridge chatBridge) {
		String m = message;
		
		if (m.isEmpty()) {
			m = String.format("%s %s!", user.name, connect ? "connected" : "disconnected");
		}
		
		chatBridge.broadcastChatMessage(source, null, m);
	}
}
