package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundResponseDeniedPayload(String clientName, int transaction, String reason) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleResponseDenied(this);
	}
}
