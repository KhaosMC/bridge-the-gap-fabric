package khaosmc.bridge.the.gap.fabric.chatbridge.request;

import java.util.List;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.ResponseC2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.response.UserListResponse;
import net.minecraft.server.network.ServerPlayerEntity;

public class UserListRequest extends Request {
	
	public UserListRequest() {
		
	}
	
	@Override
	public void handle(Client client, ChatBridge chatBridge) {
		List<ServerPlayerEntity> players = chatBridge.mcServer.getPlayerManager().getPlayerList();
		User[] users = new User[players.size()];
		
		int index = 0;
		
		for (ServerPlayerEntity player : players) {
			users[index++] = User.fromPlayer(player);
		}
		
		UserListResponse response = new UserListResponse(users);
		ResponseC2SPacket packet = new ResponseC2SPacket(response);
		chatBridge.sendPacket(packet, client);
	}
}
