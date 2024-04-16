package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import net.minecraft.server.level.ServerPlayer;

public record ServerBoundUserDisconnectPayload(String id, String name, int color) implements ServerBoundPayload {

	public ServerBoundUserDisconnectPayload(ServerPlayer player) {
		this(
			player.getScoreboardName(),
			player.getDisplayName().getString(),
			player.getTeamColor()
		);
	}
}
