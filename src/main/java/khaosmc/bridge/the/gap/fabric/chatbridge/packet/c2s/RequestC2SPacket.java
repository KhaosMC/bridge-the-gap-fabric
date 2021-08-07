package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;

public class RequestC2SPacket extends C2SPacket {
	
	public Request request;
	
	public RequestC2SPacket(Request request) {
		this.request = request;
	}
}
