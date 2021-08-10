package khaosmc.bridge.the.gap.fabric.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import khaosmc.bridge.the.gap.fabric.chatbridge.ChatBridge;
import khaosmc.bridge.the.gap.fabric.chatbridge.Client;
import khaosmc.bridge.the.gap.fabric.chatbridge.TextHelper;
import khaosmc.bridge.the.gap.fabric.chatbridge.User;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.WhisperC2SPacket;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class WhisperCommand {
	
	public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.
			literal("whisper").
			requires(context -> {
				if (!ChatBridge.isConnected()) {
					return false;
				}
				
				Entity entity = context.getEntity();
				
				if (entity == null) {
					return false;
				}
				
				return entity instanceof ServerPlayerEntity;
			}).
			then(CommandManager.
				argument("client", StringArgumentType.word()).
				suggests((context, suggestionsBuilder) -> CommandSource.suggestMatching(ChatBridge.getInstance().getClientNames(), suggestionsBuilder)).
				then(CommandManager.
					argument("user", StringArgumentType.string()).
					suggests((context, suggestionsBuilder) -> CommandSource.suggestMatching(ChatBridge.getInstance().getUserNames(parseClient(context, "client").name), suggestionsBuilder)).
					then(CommandManager.
						argument("message", StringArgumentType.greedyString()).
						executes(context -> whisper(context.getSource(), parseTarget(context, "client", "user"), StringArgumentType.getString(context, "message"))))));
		
		dispatcher.register(builder);
	}
	
	private static Client parseClient(CommandContext<ServerCommandSource> context, String arg) throws CommandSyntaxException {
		String clientName = StringArgumentType.getString(context, arg);
		Client client = ChatBridge.getInstance().getClient(clientName);
		
		if (client == null) {
			throw new DynamicCommandExceptionType(value -> new LiteralMessage(String.format("Unknown client \'%s\'", value))).create(clientName);
		}
		
		return client;
	}
	
	private static Target parseTarget(CommandContext<ServerCommandSource> context, String clientArg, String userArg) throws CommandSyntaxException {
		Client client = parseClient(context, clientArg);
		
		if (userArg == null) {
			return new Target(client, null);
		}
		
		String userName = StringArgumentType.getString(context, userArg);
		
		if (userName.equals("*")) {
			return new Target(client, null);
		}
		
		User user = ChatBridge.getInstance().getUser(client.name, userName);
		
		if (user == null) {
			throw new DynamicCommandExceptionType(value -> new LiteralMessage(String.format("Unknown user \'%s\'", value))).create(userName);
		}
		
		return new Target(client, user);
	}
	
	private static int whisper(ServerCommandSource source, Target target, String message) throws CommandSyntaxException {
		ServerPlayerEntity player = source.getPlayer();
		Text feedback = getWhisperFeedback(target.client, target.user, message);
		player.sendSystemMessage(feedback, Util.NIL_UUID);
		
		WhisperC2SPacket packet = new WhisperC2SPacket(User.fromPlayer(player), target.user, message);
		ChatBridge.getInstance().sendPacket(packet, target.client);
		
		return 1;
	}
	
	private static Text getWhisperFeedback(Client client, User user, String message) {
		MutableText text = new LiteralText("You whisper to ").formatted(Formatting.GRAY, Formatting.ITALIC);
		
		if (client != null) {
			text.append(TextHelper.fancyFormatClientName(client));
		}
		if (user != null) {
			text.append(" ").
				append(TextHelper.formatUserName(client, user, false));
		}
		text.append(String.format(": %s", message));
		
		return text;
	}
	
	private static class Target {
		
		public final Client client;
		public final User user;
		
		public Target(Client client, User user) {
			this.client = client;
			this.user = user;
		}
	}
}
