package khaosmc.bridge.the.gap.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import khaosmc.bridge.the.gap.chat.Components;
import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundClientWhisperPayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundPayload;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundUserWhisperPayload;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class WhisperCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
		LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.
			literal("whisper").
			requires(source -> BridgeTheGapClient.isConnected() && (source.getEntity() == null || source.isPlayer())).
			then(Commands.
				argument("client", StringArgumentType.word()).
//TODO				suggests((context, builder) -> SharedSuggestionProvider.suggest(getClientNames(), builder)).
				then(Commands.
					argument("message", StringArgumentType.greedyString()).
					executes(context -> whisper(context.getSource(), StringArgumentType.getString(context, "client"), null, "message"))).
				then(Commands.
					argument("user", StringArgumentType.string()).
//TODO					suggests((context, builder) -> SharedSuggestionProvider.suggest(getUserNames(StringArgumentType.getString(context, "client")), builder)).
					then(Commands.
						argument("message", StringArgumentType.greedyString()).
						executes(context -> whisper(context.getSource(), StringArgumentType.getString(context, "client"), StringArgumentType.getString(context, "user"), StringArgumentType.getString(context, "message"))))));
		
		dispatcher.register(argumentBuilder);
	}

	private static int whisper(CommandSourceStack source, String targetClient, String targetUser, String message) throws CommandSyntaxException {
		MinecraftServer server = source.getServer();
		ServerPlayer player = source.getPlayer();

		MutableComponent feedback = Component
			.literal("You whisper to ")
			.append(Components.clientName(targetClient))
			.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
		if (targetUser != null) {
			feedback
				.append(Components.userName(targetUser))
				.withStyle(ChatFormatting.ITALIC);
		}
		feedback
			.append(Component
				.literal(": ")
				.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
			.append(Component
				.literal(message)
				.withStyle(ChatFormatting.ITALIC));

		if (player == null) {
			server.sendSystemMessage(feedback);
		} else {
			player.sendSystemMessage(feedback);
		}

		ServerBoundPayload whisperPacket;

		if (player == null) {
			whisperPacket = new ServerBoundClientWhisperPayload(targetClient, targetUser, message);
		} else {
			whisperPacket = new ServerBoundUserWhisperPayload(player, targetClient, targetUser, message);
		}

		BridgeTheGapClient.getInstance().sendPacket(whisperPacket);

		return Command.SINGLE_SUCCESS;
	}
}
