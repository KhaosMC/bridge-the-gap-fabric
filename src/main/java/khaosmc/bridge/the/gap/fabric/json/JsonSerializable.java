package khaosmc.bridge.the.gap.fabric.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface JsonSerializable {
	
	default void decode(Gson gson, JsonObject json) {
		
	}
}
