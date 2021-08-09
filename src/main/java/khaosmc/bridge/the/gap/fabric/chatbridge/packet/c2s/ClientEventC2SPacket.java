package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.event.client.ClientEvent;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ClientEventC2SPacket extends C2SPacket {
	
	public String type;
	public ClientEvent event;
	
	protected ClientEventC2SPacket(ClientEvent event) {
		this.type = Registries.getId(Registries.CLIENT_EVENTS, event.getClass());
		this.event = event;
	}
}
