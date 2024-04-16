package khaosmc.bridge.the.gap.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import khaosmc.bridge.the.gap.command.WhisperCommand;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;

@Mixin(Commands.class)
public class CommandsMixin {

	@Shadow @Final
	private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(
		method="<init>",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"
		)
	)
	private void btg$registerCommands(CommandSelection selection, CommandBuildContext buildContext, CallbackInfo ci) {
		WhisperCommand.register(dispatcher, buildContext);
	}
}