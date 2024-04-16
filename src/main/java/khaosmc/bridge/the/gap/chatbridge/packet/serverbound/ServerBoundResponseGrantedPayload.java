package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import khaosmc.bridge.the.gap.chatbridge.packet.Responses;
import khaosmc.bridge.the.gap.chatbridge.packet.response.Response;

public record ServerBoundResponseGrantedPayload(String targetClient, int transaction, int response, Response data) implements ServerBoundPayload {

	public ServerBoundResponseGrantedPayload(String targetClient, int transaction, Response response) {
		this(
			targetClient,
			transaction,
			Responses.REGISTRY.getId(response.getClass()),
			response
		);
	}
}
