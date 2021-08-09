package khaosmc.bridge.the.gap.fabric.chatbridge;

import net.minecraft.server.network.ServerPlayerEntity;

public class User {
	
	public String id;
	public String name;
	
	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User other = (User)o;
			return id.equals(other.id);
		}
		
		return false;
	}
	
	public static User fromPlayer(ServerPlayerEntity player) {
		String id = player.getUuidAsString();
		String name = player.getEntityName();
		
		return new User(id, name);
	}
}
