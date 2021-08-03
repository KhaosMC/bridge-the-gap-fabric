package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;

public class AuthS2CPacket extends S2CPacket {
	
	public boolean success;
	public String reason;
	
	@Override
	public void execute(ChatBridge chatBridge) {
		String log;
		
		if (success) {
			log = "Successfully connected to the websocket";
		} else {
			log = "Failed to connect to the websocket";
		}
		if (!reason.isEmpty()) {
			log = String.format("%s: %s", log , reason);
		}
		
		BridgeTheGapMod.LOGGER.info(log);
	}
}
