package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import net.minecraft.server.level.ServerPlayer;

public record ServerBoundUserConnectPayload(String id, String name, int color) implements ServerBoundPayload {

	public ServerBoundUserConnectPayload(ServerPlayer player) {
		this(
			player.getScoreboardName(),
			player.getDisplayName().getString(),
			player.getTeamColor()
		);
	}
}
