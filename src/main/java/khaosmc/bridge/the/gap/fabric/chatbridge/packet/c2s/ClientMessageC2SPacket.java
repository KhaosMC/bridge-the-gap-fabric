package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

public class ClientMessageC2SPacket extends C2SPacket {
	
	public String message;
	
	public ClientMessageC2SPacket(String message) {
		this.message = message;
	}
}
