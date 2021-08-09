package khaosmc.bridge.the.gap.fabric.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonHelper {
	
	private static final Gson GSON = new Gson();
	
	public static JsonElement toJson(Object obj) {
		return GSON.toJsonTree(obj);
	}
	
	public static JsonElement toJson(String string) {
		return fromJson(string, JsonElement.class);
	}
	
	public static <T> T fromJson(String string, Class<T> clazz) {
		return GSON.fromJson(string, clazz);
	}
	
	public static <T> T fromJson(JsonElement rawJson, Class<T> clazz) {
		return GSON.fromJson(rawJson, clazz);
	}
}
