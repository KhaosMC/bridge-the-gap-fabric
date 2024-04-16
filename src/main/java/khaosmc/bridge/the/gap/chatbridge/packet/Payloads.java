package khaosmc.bridge.the.gap.chatbridge.packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import khaosmc.bridge.the.gap.chatbridge.packet.clientbound.*;
import khaosmc.bridge.the.gap.chatbridge.packet.serverbound.*;

public final class Payloads<T> {

	public static final Gson GSON = new GsonBuilder().create();

	public static final Payloads<ClientBoundPayload> CLIENT_BOUND = new Payloads<>(builder -> {
		builder.register(ClientBoundClientConnectPayload.class);
		builder.register(ClientBoundClientDisconnectPayload.class);
		builder.register(ClientBoundUserConnectPayload.class);
		builder.register(ClientBoundUserDisconnectPayload.class);
		builder.register(ClientBoundClientMessagePayload.class);
		builder.register(ClientBoundUserMessagePayload.class);
		builder.register(ClientBoundClientWhisperPayload.class);
		builder.register(ClientBoundUserWhisperPayload.class);
		builder.register(ClientBoundRequestPayload.class);
		builder.register(ClientBoundResponseDeniedPayload.class);
		builder.register(ClientBoundResponseGrantedPayload.class);
	});
	public static final Payloads<ServerBoundPayload> SERVER_BOUND = new Payloads<>(builder -> {
		builder.register(ServerBoundClientConnectPayload.class);
		builder.register(ServerBoundClientDisconnectPayload.class);
		builder.register(ServerBoundUserConnectPayload.class);
		builder.register(ServerBoundUserDisconnectPayload.class);
		builder.register(ServerBoundClientMessagePayload.class);
		builder.register(ServerBoundUserMessagePayload.class);
		builder.register(ServerBoundClientWhisperPayload.class);
		builder.register(ServerBoundUserWhisperPayload.class);
		builder.register(ServerBoundRequestPayload.class);
		builder.register(ServerBoundResponseDeniedPayload.class);
		builder.register(ServerBoundResponseGrantedPayload.class);
	});

	public static String serialize(ServerBoundPayload payload) throws IOException {
		try {
			Class<? extends ServerBoundPayload> type = payload.getClass();

			int id = SERVER_BOUND.getId(type);
			JsonElement payloadJson = GSON.toJsonTree(payload);

			JsonObject packetJson = new JsonObject();

			packetJson.addProperty("packet", id);
			packetJson.add("payload", payloadJson);

			return packetJson.toString();
		} catch (Exception e) {
			throw new IOException("error serializing outgoing packet", e);
		}
	}

	public static ClientBoundPayload deserialize(String s) throws IOException {
		try {
			JsonObject packetJson = GSON.fromJson(s, JsonObject.class);

			int id = packetJson.get("packet").getAsInt();
			JsonElement payloadJson = packetJson.get("payload");

			Class<? extends ClientBoundPayload> type = CLIENT_BOUND.getType(id);

			return GSON.fromJson(payloadJson, type);
		} catch (Exception e) {
			throw new IOException("error deserializing incoming packet", e);
		}
	}

	private final List<Class<? extends T>> types = new ArrayList<>();
	private final Object2IntMap<Class<? extends T>> ids = new Object2IntOpenHashMap<>();

	private Payloads(Consumer<Payloads<T>> builder) {
		builder.accept(this);
	}

	private void register(Class<? extends T> type) {
		if (ids.containsKey(type)) {
			throw new IllegalArgumentException("payload " + type.getSimpleName() + " was already registered, with id " + ids.getInt(type));
		}

		int id = types.size() + 1;

		types.add(type);
		ids.put(type, id);
	}

	public int getId(Class<? extends T> type) {
		return ids.getInt(type);
	}

	public Class<? extends T> getType(int id) {
		return types.get(id);
	}
}
