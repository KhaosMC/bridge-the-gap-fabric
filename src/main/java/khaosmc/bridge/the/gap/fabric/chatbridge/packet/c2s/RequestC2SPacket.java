package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class RequestC2SPacket extends C2SPacket {
	
	public String type;
	public Request request;
	
	public RequestC2SPacket(Request request) {
		this.type = Registries.getId(Registries.REQUESTS, request.getClass());
		this.request = request;
	}
}
