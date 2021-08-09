package khaosmc.bridge.the.gap.fabric.json;

import com.google.gson.JsonElement;

public interface JsonSerializable {
	
	default void decode(JsonElement rawJson) {
		
	}
}
