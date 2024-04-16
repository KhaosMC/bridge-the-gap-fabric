package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;
import khaosmc.bridge.the.gap.chatbridge.Platform;

public record ClientBoundClientConnectPayload(Platform platform, String name, int color) implements ClientBoundPayload {

	@Override
	public void handle(PacketHandler handler) {
		handler.handleClientConnect(this);
	}
}
