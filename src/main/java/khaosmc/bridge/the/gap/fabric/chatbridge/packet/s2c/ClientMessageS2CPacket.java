package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;

public class ClientMessageS2CPacket extends S2CPacket {
	
	public String message;
	
	@Override
	public void execute(ChatBridge chatBridge) {
		chatBridge.broadcastChatMessage(source, null, message);
	}
}
