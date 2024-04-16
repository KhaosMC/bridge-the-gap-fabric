package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundUserConnectPayload(String clientName, int clientColor, String userId, String userName, int userColor) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleUserConnect(this);
	}
}
