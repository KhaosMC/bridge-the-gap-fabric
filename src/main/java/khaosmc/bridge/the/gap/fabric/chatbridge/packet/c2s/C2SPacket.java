package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.Client;

public class C2SPacket {
	
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
