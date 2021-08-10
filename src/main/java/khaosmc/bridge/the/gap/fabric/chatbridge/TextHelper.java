package khaosmc.bridge.the.gap.fabric.chatbridge;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;

public class TextHelper {
	
	public static Text fancyFormatClientName(Client client) {
		return new LiteralText("[").
			append(formatClientName(client, false)).
			append("]");
	}
	
	public static MutableText formatClientName(Client client, boolean colorOnly) {
		MutableText text = new LiteralText(client.name);
		Integer color = client.getColor();
		
		if (color != null) {
			text.styled(style -> style.
				withColor(TextColor.fromRgb(color)));
		}
		if (!colorOnly) {
			text.styled(style -> style.
				withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to whisper to [").
					append(formatClientName(client, true)).
					append("]"))).
				withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/whisper %s \"*\" ", client.name))));
		}
		
		return text;
	}
	
	public static Text fancyFormatUserName(Client client, User user) {
		return new LiteralText("<").
			append(formatUserName(client, user, false)).
			append(">");
	}
	
	public static Text formatUserName(Client client, User user, boolean colorOnly) {
		MutableText text = new LiteralText(user.name);
		Integer color = user.getColor();
		
		if (color != null) {
			text.styled(style -> style.
				withColor(TextColor.fromRgb(color)));
		}
		if (!colorOnly) {
			text.styled(style -> style.
				withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to whisper to ").
					append(formatUserName(client, user, true)))).
				withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/whisper %s \"%s\" ", client.name, user.name))));
		}
		
		return text;
	}
	
	public static Text getJoinMessage(MinecraftServer server, ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		String name = profile.getName();
		String cachedName = getCachedPlayerName(server, profile);
		Text displayName = player.getDisplayName();
		
		if (cachedName == null || name.equalsIgnoreCase(cachedName)) {
			return getJoinMessage(displayName);
		} else {
			return getJoinMessage(displayName, cachedName);
		}
	}
	
	private static String getCachedPlayerName(MinecraftServer server, GameProfile profile) {
		UserCache userCache = server.getUserCache();
		GameProfile cachedProfile = userCache.getByUuid(profile.getId());
		
		return (cachedProfile == null) ? profile.getName() : cachedProfile.getName();
	}
	
	public static Text getJoinMessage(Text playerName) {
		return getJoinMessage(playerName, null);
	}
	
	public static Text getJoinMessage(Text playerName, String prevName) {
		TranslatableText joinMessage;
		
		if (prevName == null) {
			joinMessage = new TranslatableText("multiplayer.player.joined", playerName);
		} else {
			joinMessage = new TranslatableText("multiplayer.player.joined.renamed", playerName, prevName);
		}
		
		return joinMessage.formatted(Formatting.YELLOW);
	}
	
	public static Text getLeaveMessage(ServerPlayerEntity player) {
		return getLeaveMessage(player.getDisplayName());
	}
	
	public static Text getLeaveMessage(Text playerName) {
		return new TranslatableText("multiplayer.player.left", playerName).formatted(Formatting.YELLOW);
	}
}
