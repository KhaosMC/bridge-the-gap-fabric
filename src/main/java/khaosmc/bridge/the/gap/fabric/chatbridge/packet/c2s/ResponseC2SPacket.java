package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;

public class ResponseC2SPacket extends C2SPacket {
	
	public Response response;
	
	public ResponseC2SPacket(Response response) {
		this.response = response;
	}
}
