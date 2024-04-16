package khaosmc.bridge.the.gap.chatbridge.packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import khaosmc.bridge.the.gap.chatbridge.packet.request.*;

public final class Requests {

	public static final Requests REGISTRY = new Requests(builder -> {
	});

	public static Request deserialize(int id, String s) throws IOException {
		try {
			return Payloads.GSON.fromJson(s, REGISTRY.getType(id));
		} catch (Exception e) {
			throw new IOException("error deserializing request " + id, e);
		}
	}

	private final List<Class<? extends Request>> types = new ArrayList<>();
	private final Object2IntMap<Class<? extends Request>> ids = new Object2IntOpenHashMap<>();

	private Requests(Consumer<Requests> builder) {
		builder.accept(this);
	}

	private void register(Class<? extends Request> type) {
		if (ids.containsKey(type)) {
			throw new IllegalArgumentException("request " + type.getSimpleName() + " was already registered, with id " + ids.getInt(type));
		}

		int id = types.size() + 1;

		types.add(type);
		ids.put(type, id);
	}

	public int getId(Class<? extends Request> type) {
		return ids.getInt(type);
	}

	public Class<? extends Request> getType(int id) {
		return types.get(id);
	}
}
