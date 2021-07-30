package khaosmc.bridge.the.gap.fabric;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class BTGClient extends WebSocketClient {
	
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
}
