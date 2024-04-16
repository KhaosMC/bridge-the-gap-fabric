package khaosmc.bridge.the.gap.chatbridge.packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import khaosmc.bridge.the.gap.chatbridge.packet.response.*;

public final class Responses {

	public static final Responses REGISTRY = new Responses(builder -> {
	});

	public static Response deserialize(int id, String s) throws IOException {
		try {
			return Payloads.GSON.fromJson(s, REGISTRY.getType(id));
		} catch (Exception e) {
			throw new IOException("error deserializing response " + id, e);
		}
	}

	private final List<Class<? extends Response>> types = new ArrayList<>();
	private final Object2IntMap<Class<? extends Response>> ids = new Object2IntOpenHashMap<>();

	private Responses(Consumer<Responses> builder) {
		builder.accept(this);
	}

	private void register(Class<? extends Response> type) {
		if (ids.containsKey(type)) {
			throw new IllegalArgumentException("response " + type.getSimpleName() + " was already registered, with id " + ids.getInt(type));
		}

		int id = types.size() + 1;

		types.add(type);
		ids.put(type, id);
	}

	public int getId(Class<? extends Response> type) {
		return ids.getInt(type);
	}

	public Class<? extends Response> getType(int id) {
		return types.get(id);
	}
}
