package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

public record ServerBoundResponseDeniedPayload(String targetClient, int transaction, String reason) implements ServerBoundPayload {
}
