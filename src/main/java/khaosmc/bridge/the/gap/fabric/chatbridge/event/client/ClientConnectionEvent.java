package khaosmc.bridge.the.gap.fabric.chatbridge.event.client;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.TextHelper;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ClientConnectionEvent extends ClientEvent {
	
	public boolean connect;
	
	public ClientConnectionEvent(boolean connect) {
		this.connect = connect;
	}
	
	@Override
	public void handle(Client client, ChatBridge chatBridge) {
		if (connect) {
			chatBridge.onClientConnect(client);
		} else {
			chatBridge.onClientDisconnect(client);
		}
		
		Text text = new LiteralText(String.format("%s client ", client.type)).formatted(Formatting.YELLOW).
			append(TextHelper.fancyFormatClientName(client)).
			append(String.format(" %s the chat bridge!", connect ? "connected to" : "disconnected from"));
		
		chatBridge.broadcastChatMessage(null, null, text);
	}
}
