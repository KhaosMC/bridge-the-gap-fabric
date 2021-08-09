package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public abstract class S2CPacket implements JsonSerializable {
	
	public static void registerPackets(Registry<S2CPacket> registry) {
		Registries.register(registry, "client_event"  , ClientEventS2CPacket.class);
		Registries.register(registry, "user_event"    , UserEventS2CPacket.class);
		Registries.register(registry, "client_message", ClientMessageS2CPacket.class);
		Registries.register(registry, "chat_message"  , ChatMessageS2CPacket.class);
		Registries.register(registry, "request"       , RequestS2CPacket.class);
		Registries.register(registry, "response"      , ResponseS2CPacket.class);
	}
	
	public abstract void execute(Client source, ChatBridge chatBridge);
	
}
