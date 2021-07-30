package khaosmc.bridge.the.gap.fabric.mixin;

import java.net.Proxy;
import java.net.URI;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;

import khaosmc.bridge.the.gap.fabric.BTGClient;
import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;
import khaosmc.bridge.the.gap.fabric.config.Config;
import khaosmc.bridge.the.gap.fabric.config.ConfigManager;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager.Impl;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage.Session;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin extends MinecraftServer {

	private Config config;
	private BTGClient client;
	
	public MinecraftDedicatedServerMixin(Thread thread, Impl impl, Session session, SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
		super(thread, impl, session, saveProperties, resourcePackManager, proxy, dataFixer, serverResourceManager, minecraftSessionService, gameProfileRepository, userCache, worldGenerationProgressListenerFactory);
	}
	
	@Inject(
			method = "setupServer",
			at = @At(
					value = "RETURN"
			)
	)
	private void onSetupServer(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			config = ConfigManager.loadConfig(getRunDirectory());
			
			if (ConfigManager.isInvalid(config)) {
				BridgeTheGapMod.LOGGER.warn("Please fill in the config file and restart the server!");
			} else {
				connectToChatBridge();
			}
		}
	}
	
	private void connectToChatBridge() {
		client = new BTGClient(URI.create(config.server_url));
		client.connect();
	}
}
