package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundUserWhisperPayload(String clientName, int clientColor, String userId, String userName, int userColor, String targetUser, String message) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleUserWhisper(this);
	}
}
