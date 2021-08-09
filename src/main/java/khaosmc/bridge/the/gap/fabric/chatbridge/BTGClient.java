package khaosmc.bridge.the.gap.fabric.chatbridge;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.AuthS2CMessage;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.Message;
import khaosmc.bridge.the.gap.fabric.chatbridge.message.S2CMessage;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;

public class BTGClient extends WebSocketClient {
	
	public BTGClient(URI serverUri) {
		super(serverUri);
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		BridgeTheGapMod.LOGGER.info(handshakedata.getHttpStatusMessage());
		ChatBridge.getInstance().onServerHandshake();
	}
	
	@Override
	public void onMessage(String message) {
		S2CMessage serverMessage = JsonHelper.fromJson(message, S2CMessage.class);
		
		if (serverMessage.type == null) {
			BridgeTheGapMod.LOGGER.error("Unable to decode message from websocket server - unknown format");
			return;
		}
		
		if (serverMessage.type.equals("auth")) {
			AuthS2CMessage auth = JsonHelper.fromJson(message, AuthS2CMessage.class);
			ChatBridge.getInstance().onAuth(auth);
		} else {
			ChatBridge.getInstance().onMessageReceived(serverMessage);
		}
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		BridgeTheGapMod.LOGGER.info(reason);
	}
	
	@Override
	public void onError(Exception ex) {
		BridgeTheGapMod.LOGGER.error(ex);
	}
	
	public void sendMessage(Message message) {
		send(JsonHelper.toJson(message).toString());
	}
}
