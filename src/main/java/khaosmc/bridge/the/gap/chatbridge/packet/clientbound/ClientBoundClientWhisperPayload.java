package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;

public record ClientBoundClientWhisperPayload(String clientName, int clientColor, String targetUser, String message) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleClientWhisper(this);
	}
}
