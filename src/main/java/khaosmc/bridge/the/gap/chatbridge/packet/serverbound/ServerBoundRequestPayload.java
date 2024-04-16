package khaosmc.bridge.the.gap.chatbridge.packet.serverbound;

import khaosmc.bridge.the.gap.chatbridge.packet.Requests;
import khaosmc.bridge.the.gap.chatbridge.packet.request.Request;

public record ServerBoundRequestPayload(String targetClient, int transaction, int request, Request data) implements ServerBoundPayload {

	public ServerBoundRequestPayload(String targetClient, int transaction, Request request) {
		this(
			targetClient,
			transaction,
			Requests.REGISTRY.getId(request.getClass()),
			request
		);
	}
}
