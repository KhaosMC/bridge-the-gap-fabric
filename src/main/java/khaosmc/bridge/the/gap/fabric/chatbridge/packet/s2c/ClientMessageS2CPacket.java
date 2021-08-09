package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class ClientMessageS2CPacket extends S2CPacket {
	
	public String message;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		chatBridge.broadcastChatMessage(source, null, message);
	}
}
