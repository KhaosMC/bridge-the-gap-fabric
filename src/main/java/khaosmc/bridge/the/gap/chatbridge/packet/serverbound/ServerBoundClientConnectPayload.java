package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;
import khaosmc.bridge.the.gap.chatbridge.Platform;

public record ServerBoundClientConnectPayload(String platform, String name, int color) implements ServerBoundPayload {

	public ServerBoundClientConnectPayload() {
		this(
			Platform.MINECRAFT.name(),
			BridgeTheGapClient.getInstance().getName(),
			BridgeTheGapClient.getInstance().getColor()
		);
	}
}
