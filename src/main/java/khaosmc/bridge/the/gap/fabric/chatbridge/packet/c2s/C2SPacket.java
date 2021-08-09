package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public class C2SPacket {
	
	public static void registerPackets(Registry<C2SPacket> registry) {
		Registries.register(registry, "client_event"  , ClientEventC2SPacket.class);
		Registries.register(registry, "user_event"    , UserEventC2SPacket.class);
		Registries.register(registry, "client_message", ClientMessageC2SPacket.class);
		Registries.register(registry, "chat_message"  , ChatMessageC2SPacket.class);
		Registries.register(registry, "request"       , RequestC2SPacket.class);
		Registries.register(registry, "response"      , ResponseC2SPacket.class);
	}
}
