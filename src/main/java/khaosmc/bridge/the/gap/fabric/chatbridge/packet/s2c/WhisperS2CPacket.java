package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class WhisperS2CPacket extends S2CPacket {
	
	public User fromUser;
	public User toUser;
	public String message;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		chatBridge.broadcastWhisper(source, fromUser, toUser, message);
	}
}
