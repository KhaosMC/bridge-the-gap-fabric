package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;

public class BTGClient extends WebSocketClient {
	
	public BTGClient(URI serverUri) {
		super(serverUri);
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		BridgeTheGapMod.LOGGER.info(handshakedata.getHttpStatusMessage());
		ChatBridge.getInstance().tryAuth();
	}
	
	@Override
	public void onMessage(String message) {
		ChatBridge.getInstance().onPacketReceived(message);
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		BridgeTheGapMod.LOGGER.info(reason);
	}
	
	@Override
	public void onError(Exception ex) {
		BridgeTheGapMod.LOGGER.error(ex);
	}
}
