package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

public record ServerBoundClientMessagePayload(String message) implements ServerBoundPayload {
}
