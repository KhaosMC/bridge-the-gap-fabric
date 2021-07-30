package khaosmc.bridge.the.gap.fabric.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import khaosmc.bridge.the.gap.fabric.BridgeTheGapMod;

public class ConfigManager {
	
	private static final String NAME = BridgeTheGapMod.MOD_ID + ".json";
	private static final Gson CONFIG_PARSER = new GsonBuilder().setPrettyPrinting().create();
	
	public static Config loadConfig(File dir) {
		File configFile = new File(dir, NAME);
		return configFile.exists() ? readConfig(configFile) : createConfig(configFile);
	}
	
	private static Config readConfig(File configFile) {
		try (FileReader reader = new FileReader(configFile)) {
			return CONFIG_PARSER.fromJson(reader, Config.class);
		} catch (IOException e) {
			return new Config();
		}
	}
	
	private static Config createConfig(File configFile) {
		Config config = new Config();
		
		try (FileWriter writer = new FileWriter(configFile)) {
			writer.write(CONFIG_PARSER.toJson(config));
		} catch (IOException e) {
			
		}
		
		return config;
	}
	
	public static boolean isInvalid(Config config) {
		return config.server_url.isEmpty() || config.client_name.isEmpty() || config.client_type.isEmpty() || config.auth_token.isEmpty();
	}
}
