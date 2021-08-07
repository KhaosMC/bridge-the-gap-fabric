package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public abstract class S2CPacket implements JsonSerializable {
	
	static {
		
		Registries.register(Registries.S2C_PACKETS, "auth"             , AuthS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "client_event"     , ClientEventS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "user_event"       , UserEventS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "client_message"   , ClientMessageS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "chat_message"     , ChatMessageS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "request"          , RequestS2CPacket.class);
		Registries.register(Registries.S2C_PACKETS, "response"         , ResponseS2CPacket.class);
		
	}
	
	public Client source;
	
	public abstract void execute(ChatBridge chatBridge);
	
}
