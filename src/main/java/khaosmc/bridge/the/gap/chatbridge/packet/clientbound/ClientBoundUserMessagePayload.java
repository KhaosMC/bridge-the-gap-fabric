package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundUserMessagePayload(String clientName, int clientColor, String userId, String userName, int userColor, String message) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleUserMessage(this);
	}
}
