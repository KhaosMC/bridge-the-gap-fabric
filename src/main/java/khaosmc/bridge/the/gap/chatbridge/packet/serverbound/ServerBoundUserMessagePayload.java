package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import net.minecraft.server.level.ServerPlayer;

public record ServerBoundUserMessagePayload(String userId, String userName, int userColor, String message) implements ServerBoundPayload {

	public ServerBoundUserMessagePayload(ServerPlayer player, String message) {
		this(
			player.getScoreboardName(),
			player.getDisplayName().getString(),
			player.getTeamColor(),
			message
		);
	}
}
