package khaosmc.bridge.the.gap.fabric.chatbridge.event.user;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.TextHelper;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class UserConnectionEvent extends UserEvent {
	
	public boolean connect;
	
	public UserConnectionEvent(boolean connect) {
		this.connect = connect;
	}
	
	@Override
	public void handle(Client client, User user, ChatBridge chatBridge) {
		if (connect) {
			chatBridge.onUserConnect(client, user);
		} else {
			chatBridge.onUserDisconnect(client, user);
		}
		
		if (client.type.equals("minecraft")) {
			Text playerName = TextHelper.formatUserName(client, user, !connect);
			Text message = connect ? TextHelper.getJoinMessage(playerName) : TextHelper.getLeaveMessage(playerName);
			
			chatBridge.broadcastChatMessage(client, null, message);
		} else {
			Text message = new LiteralText("").
				append(TextHelper.formatUserName(client, user, false)).
				append(String.format(" %s!", connect ? "connected" : "disconnected"));
			
			chatBridge.broadcastChatMessage(client, null, message);
		}
	}
}
