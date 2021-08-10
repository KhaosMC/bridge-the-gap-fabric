package khaosmc.bridge.the.gap.fabric.chatbridge.response;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;

public class UserListResponse extends Response {
	
	public User[] user_list;
	
	public UserListResponse(User[] user_list) {
		this.user_list = user_list;
	}
	
	public void handle(Client client, ChatBridge chatBridge) {
		chatBridge.updateUserList(client, user_list);
	}
}
