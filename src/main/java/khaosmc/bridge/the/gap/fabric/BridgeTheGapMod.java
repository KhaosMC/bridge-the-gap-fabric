package khaosmc.bridge.the.gap.fabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.DedicatedServerModInitializer;

public class BridgeTheGapMod implements DedicatedServerModInitializer {
	
	public static final String MOD_ID = "bridge_the_gap_fabric";
	public static final String MOD_NAME = "Bridge The Gap";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	@Override
	public void onInitializeServer() {
		LOGGER.info(String.format("Initialized %s!", MOD_NAME));
	}
}
