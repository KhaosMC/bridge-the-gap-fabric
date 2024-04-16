package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import java.io.IOException;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;
import khaosmc.bridge.the.gap.chatbridge.packet.Requests;
import khaosmc.bridge.the.gap.chatbridge.packet.request.Request;

public class ClientBoundRequestPayload implements ClientBoundPayload {

	private final String clientName;
	private final int transaction;
	private final int request;
	private final Request data;

	public ClientBoundRequestPayload(String clientName, int transaction, int request, String data) throws IOException {
		this.clientName = clientName;
		this.transaction = transaction;
		this.request = request;
		this.data = Requests.deserialize(request, data);
	}

	@Override
	public void handle(PacketHandler handler) {
		handler.handleRequest(this);
	}

	public String clientName() {
		return clientName;
	}

	public int transaction() {
		return transaction;
	}

	public int request() {
		return request;
	}

	public Request data() {
		return data;
	}
}
