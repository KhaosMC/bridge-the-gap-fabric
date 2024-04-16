package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

public record ServerBoundClientWhisperPayload(String targetClient, String targetUser, String message) implements ServerBoundPayload {
}
