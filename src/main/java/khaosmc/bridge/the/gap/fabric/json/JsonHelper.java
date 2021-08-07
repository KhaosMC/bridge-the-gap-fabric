package khaosmc.bridge.the.gap.fabric.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import khaosmc.bridge.the.gap.fabric.registry.Registries;
import khaosmc.bridge.the.gap.fabric.registry.Registry;

public class JsonHelper {
	
	private static final Gson GSON = new Gson();
	
	public static <T extends JsonSerializable> T fromJson(String s, Registry<T> registry) throws IllegalArgumentException {
		return fromJson(GSON.fromJson(s, JsonElement.class), registry);
	}
	
	public static <T extends JsonSerializable> T fromJson(JsonElement rawJson, Registry<T> registry) {
		try {
			JsonObject json = rawJson.getAsJsonObject();
			String type = json.get("type").getAsString();
			Class<? extends T> clazz = Registries.getClazz(registry, type);
			
			T obj = GSON.fromJson(json, clazz);
			obj.decode(GSON, json);
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T extends JsonSerializable> String toJson(T obj, Registry<T> registry) {
		try {
			@SuppressWarnings("unchecked")
			String type = Registries.getId(registry, (Class<? extends T>)obj.getClass());
			JsonObject json = GSON.toJsonTree(obj).getAsJsonObject();
			json.addProperty("type", type);
			
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
