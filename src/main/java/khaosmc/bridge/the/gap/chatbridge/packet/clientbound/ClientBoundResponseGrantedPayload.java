package khaosmc.bridge.the.gap.chatbridge.packet.clientbound;

import java.io.IOException;

import khaosmc.bridge.the.gap.chatbridge.PacketHandler;
import khaosmc.bridge.the.gap.chatbridge.packet.Responses;
import khaosmc.bridge.the.gap.chatbridge.packet.response.Response;

public class ClientBoundResponseGrantedPayload implements ClientBoundPayload {

	private final String clientName;
	private final int transaction;
	private final int response;
	private final Response data;

	public ClientBoundResponseGrantedPayload(String clientName, int transaction, int response, String data) throws IOException {
		this.clientName = clientName;
		this.transaction = transaction;
		this.response = response;
		this.data = Responses.deserialize(response, data);
	}

	@Override
	public void handle(PacketHandler handler) {
		handler.handleResponseGranted(this);
	}

	public String clientName() {
		return clientName;
	}

	public int transaction() {
		return transaction;
	}

	public int response() {
		return response;
	}

	public Response data() {
		return data;
	}
}
