package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import net.minecraft.server.level.ServerPlayer;

public record ServerBoundUserWhisperPayload(String userId, String userName, int userColor, String targetClient, String targetUser, String message) implements ServerBoundPayload {

	public ServerBoundUserWhisperPayload(ServerPlayer player, String targetClient, String targetUser, String message) {
		this(
			player.getScoreboardName(),
			player.getDisplayName().getString(),
			player.getTeamColor(),
			targetClient,
			targetUser,
			message
		);
	}
}
