package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundClientMessagePayload(String clientName, int clientColor, String message) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleClientMessage(this);
	}
}
