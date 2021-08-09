package khaosmc.bridge.the.gap.fabric.chatbridge;

import khaosmc.bridge.the.gap.fabric.interfaces.mixin.ITeam;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;

public class User {
	
	public String id;
	public String name;
	public String display_color; // RRGGBB
	
	public User(String id, String name, String display_color) {
		this.id = id;
		this.name = name;
		this.display_color = display_color;
	}
	
	public User(String id, String name, int color) {
		this(id, name, String.valueOf(color));
	}
	
	public User(String id, String name) {
		this(id, name, 0xFFFFFF);
	}
	
	public int getColor() {
		try {
			return Integer.parseInt(display_color, 16);
		} catch (NumberFormatException e) {
			return 0xFFFFFF;
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
		int color = 0xFFFFFF;
		
		AbstractTeam abstractTeam = player.getScoreboardTeam();
		
		if (abstractTeam != null && abstractTeam instanceof Team) {
			Integer teamColor = ((ITeam)abstractTeam).getTeamColor().getColorValue();
			
			if (teamColor != null) {
				color = teamColor;
			}
		}
		
		return new User(id, name, color);
	}
}
