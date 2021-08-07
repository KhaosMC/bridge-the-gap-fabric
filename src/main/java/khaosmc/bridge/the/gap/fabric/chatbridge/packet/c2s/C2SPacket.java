package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.json.JsonSerializable;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class C2SPacket implements JsonSerializable {
	
	static {
		
		Registries.register(Registries.C2S_PACKETS, "auth"             , AuthC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "client_event"     , ClientEventC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "user_event"       , UserEventC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "client_message"   , ClientMessageC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "chat_message"     , ChatMessageC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "request"          , RequestC2SPacket.class);
		Registries.register(Registries.C2S_PACKETS, "response"         , ResponseC2SPacket.class);
		
	}
	
	public Client[] targets;
	
	protected C2SPacket(Client[] targets) {
		this.targets = targets;
	}
	
	protected C2SPacket() {
		this(new Client[0]);
	}
	
	public void setTargets(Client[] targets) {
		this.targets = targets;
	}
	
	public void addTargets(Client... targets) {
		int newLength = this.targets.length + targets.length;
		Client[] newTargets = new Client[newLength];
		
		int index = 0;
		
		for (Client target : this.targets) {
			newTargets[index++] = target;
		}
		for (Client target : targets) {
			newTargets[index++] = target;
		}
		
		setTargets(newTargets);
	}
}
