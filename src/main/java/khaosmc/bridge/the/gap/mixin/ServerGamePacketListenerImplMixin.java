package khaosmc.bridge.the.gap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import khaosmc.bridge.the.gap.chatbridge.BridgeTheGapClient;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.ServerBoundUserDisconnectPayload;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

	@Shadow
	private ServerPlayer player;

	@Inject(
		method = "removePlayerFromWorld",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
		)
	)
	private void btg$sendPlayerLeave(CallbackInfo ci) {
		if (BridgeTheGapClient.isConnected()) {
			ServerBoundUserDisconnectPayload packet = new ServerBoundUserDisconnectPayload(player);
			BridgeTheGapClient.getInstance().sendPacket(packet);
		}
	}
}
