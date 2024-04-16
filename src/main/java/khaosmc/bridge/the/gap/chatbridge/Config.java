package khaosmc.bridge.the.gap.chatbridge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import khaosmc.bridge.the.gap.BridgeTheGapMod;

public record Config(String server_url, String auth_token, String client_name, int client_display_color) {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static Config load(Path dir) {
		Path file = dir.resolve(BridgeTheGapMod.MOD_ID + ".json");
		return Files.exists(file) ? read(file) : create(file);
	}

	private static Config read(Path file) {
		Config config = new Config();

		try (BufferedReader br = Files.newBufferedReader(file)) {
			config = GSON.fromJson(br, Config.class).check();
		} catch (IOException e) {
			BridgeTheGapMod.LOGGER.warn("unable to load chat bridge config", e);
		}

		return config;
	}

	private static Config create(Path file) {
		Config config = new Config();

		try (BufferedWriter bw = Files.newBufferedWriter(file)) {
			bw.write(GSON.toJson(config));
		} catch (IOException e) {
			BridgeTheGapMod.LOGGER.warn("unable to save chat bridge config", e);
		}

		return config;
	}

	private Config() {
		this("", "", "", 0xFFFFFF);
	}

	private Config check() {
		if (server_url.isEmpty() || auth_token.isEmpty() || client_name.isEmpty()) {
			throw new RuntimeException("please fill in the config file and restart the server!");
		}

		 return this;
	}
}
