package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public abstract class S2CPacket {
	
	public Client source;
	
	public abstract void execute(ChatBridge chatBridge);
	
}
