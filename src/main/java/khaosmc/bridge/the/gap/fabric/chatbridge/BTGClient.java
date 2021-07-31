package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import khaosmc.bridge.the.gap.fabric.chatbridge.message.Message;

public class BTGClient extends WebSocketClient {

	private static final Gson MESSAGE_PARSER = new Gson();
	
	public BTGClient(URI serverUri) {
		super(serverUri);
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		
	}
	
	@Override
	public void onMessage(String message) {
		
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		
	}
	
	@Override
	public void onError(Exception ex) {
		
	}
	
	public void sendMessage(Message message) {
		send(MESSAGE_PARSER.toJson(message));
	}
}
