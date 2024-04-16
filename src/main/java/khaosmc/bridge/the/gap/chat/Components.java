package khaosmc.bridge.the.gap.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

public class Components {

	public static Component clientName(String name) {
		return Component.literal(name).withStyle(ChatFormatting.YELLOW);
	}

	public static Component clientDisplayName(String name, int color) {
		return Component
			.literal(name)
			.withStyle(style -> style
				.withColor(color)
				.withHoverEvent(new HoverEvent(
					HoverEvent.Action.SHOW_TEXT,
					Component.literal("Click to whisper to " + name)))
				.withClickEvent(new ClickEvent(
					ClickEvent.Action.SUGGEST_COMMAND,
					"/whisper %s ".formatted(name))));
	}

	public static Component clientDecoratedName(String name, int color) {
		return Component
			.literal("[")
			.append(clientDisplayName(name, color))
			.append("]");
	}

	public static Component userName(String name) {
		return Component.literal(name).withStyle(ChatFormatting.YELLOW);
	}

	public static Component userDisplayName(String client, String id, String name, int color) {
		return Component
			.literal(name)
			.withStyle(style -> style
				.withColor(color)
				.withHoverEvent(new HoverEvent(
					HoverEvent.Action.SHOW_TEXT,
					Component.literal("Click to whisper to " + name)))
				.withClickEvent(new ClickEvent(
					ClickEvent.Action.SUGGEST_COMMAND,
					"/whisper %s %s".formatted(client, id))));
	}

	public static Component userDecoratedName(String client, String id, String name, int color) {
		return Component
			.literal("<")
			.append(userDisplayName(client, id, name, color))
			.append(">");
	}
}
