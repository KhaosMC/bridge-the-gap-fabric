package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class ChatMessageS2CPacket extends S2CPacket {
	
	public User user;
	public String message;
	
	@Override
	public void execute(ChatBridge chatBridge) {
		chatBridge.broadcastChatMessage(source, user, message);
	}
}
