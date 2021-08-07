package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

public class ClientEventC2SPacket extends C2SPacket {
	
	public String client_event;
	
	protected ClientEventC2SPacket(String client_event) {
		this.client_event = client_event;
	}
}
