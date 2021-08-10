package khaosmc.bridge.the.gap.fabric.chatbridge;

import khaosmc.bridge.the.gap.fabric.interfaces.mixin.ITeam;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;

public class User {
	
	public String id; // ids should be unique within a platform (thought not necessarily across platforms)
	public String name;
	public String display_color; // RRGGBB
	
	public User(String id, String name, String display_color) {
		this.id = id;
		this.name = name;
		this.display_color = display_color;
	}
	
	public User(String id, String name, int color) {
		this(id, name, Integer.toHexString(color));
	}
	
	public User(String id, String name) {
		this(id, name, "");
	}
	
	public Integer getColor() {
		try {
			return Integer.parseInt(display_color, 16);
		} catch (NumberFormatException e) {
			return null;
		}
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
		
		AbstractTeam team = player.getScoreboardTeam();
		
		if (team != null) {
			Integer teamColor = ((ITeam)team).getTeamColor().getColorValue();
			
			if (teamColor != null) {
				return new User(id, name, teamColor);
			}
		}
		
		return new User(id, name);
	}
}
