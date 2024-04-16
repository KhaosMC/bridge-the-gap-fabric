package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;
import khaosmc.bridge.the.gap.chatbridge.Platform;

public record ServerBoundClientDisconnectPayload(String platform, String name, int color) implements ServerBoundPayload {

	public ServerBoundClientDisconnectPayload() {
		this(
			Platform.MINECRAFT.name(),
			BridgeTheGapClient.getInstance().getName(),
			BridgeTheGapClient.getInstance().getColor()
		);
	}
}
