package khaosmc.bridge.the.gap.fabric.util;

import java.util.HashMap;
import java.util.Map;

public class Registry<T> {
	
	private final Map<String, Class<? extends T>> idToClazz;
	private final Map<Class<? extends T>, String> clazzToId;
	
	public Registry() {
		this.idToClazz = new HashMap<>();
		this.clazzToId = new HashMap<>();
	}
	
	public void register(String id, Class<? extends T> clazz) {
		if (idToClazz.containsKey(id)) {
			throw new IllegalStateException("An element with id \'" + id + "\' already exists!");
		}
		if (clazzToId.containsKey(clazz)) {
			throw new IllegalStateException("An element with clazz \'" + clazz + "\' already exists!");
		}
		
		idToClazz.put(id, clazz);
		clazzToId.put(clazz, id);
	}
	
	public Class<? extends T> getClazz(String id) {
		return idToClazz.get(id);
	}
	
	public String getId(Class<? extends T> clazz) {
		return clazzToId.get(clazz);
	}
}
