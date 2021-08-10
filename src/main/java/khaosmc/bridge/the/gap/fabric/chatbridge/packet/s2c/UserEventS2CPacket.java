package khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c;

import com.google.gson.JsonElement;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.json.JsonHelper;
import khaosmc.bridge.the.gap.fabric.registry.Registries;

public class UserEventS2CPacket extends S2CPacket {
	
	public String type;
	public User user;
	public JsonElement event;
	
	@Override
	public void execute(Client source, ChatBridge chatBridge) {
		Class<? extends UserEvent> clazz = Registries.getClazz(Registries.USER_EVENTS, type);
		UserEvent event = JsonHelper.fromJson(this.event, clazz);
		
		if (event != null) {
			event.handle(source, user, chatBridge);
		}
	}
}
