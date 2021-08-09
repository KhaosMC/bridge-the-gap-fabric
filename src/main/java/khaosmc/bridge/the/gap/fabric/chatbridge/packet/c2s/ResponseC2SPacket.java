package khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s;

import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class ResponseC2SPacket extends C2SPacket {
	
	public String type;
	public Response response;
	
	public ResponseC2SPacket(Response response) {
		this.type = Registries.getId(Registries.RESPONSES, response.getClass());
		this.response = response;
	}
}
