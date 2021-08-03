package khaosmc.bridge.the.gap.fabric.chatbridge;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;

public class User {
	
	public String id;
	public String name;
	public int display_color;
	
	public User(String id, String name, int display_color) {
		this.id = id;
		this.name = name;
		this.display_color = display_color;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User other = (User)o;
			return id.equals(other.id);
		}
		
		return false;
	}
	
	public User(String id, String name) {
		this(id, name, 0);
	}
	
	public static User fromPlayer(ServerPlayerEntity player) {
		String id = player.getUuidAsString();
		String name = player.getEntityName();
		int color = 0xFFFFFF;
		
		AbstractTeam team = player.getScoreboardTeam();
		
		if (team != null) {
			Integer teamColor = team.getColor().getColorValue();
			
			if (teamColor != null) {
				color = teamColor;
			}
		}
		
		return new User(id, name, color);
	}
}
