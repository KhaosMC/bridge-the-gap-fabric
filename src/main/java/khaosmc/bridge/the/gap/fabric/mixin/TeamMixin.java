package khaosmc.bridge.the.gap.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import khaosmc.bridge.the.gap.fabric.interfaces.mixin.ITeam;

import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

@Mixin(Team.class)
public class TeamMixin implements ITeam {
	
	@Shadow private Formatting color;
	
	@Override
	public Formatting getTeamColor() {
		return color;
	}
}
