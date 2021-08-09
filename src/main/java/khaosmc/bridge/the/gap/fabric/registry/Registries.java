package khaosmc.bridge.the.gap.fabric.registry;

import java.util.function.Consumer;

import khaosmc.bridge.the.gap.fabric.chatbridge.event.client.ClientEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.event.user.UserEvent;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.C2SPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c.S2CPacket;
import khaosmc.bridge.the.gap.fabric.chatbridge.request.Request;
import khaosmc.bridge.the.gap.fabric.chatbridge.response.Response;

public class Registries {
	
	public static final Registry<C2SPacket> C2S_PACKETS = create(C2SPacket::registerPackets);
	public static final Registry<S2CPacket> S2C_PACKETS = create(S2CPacket::registerPackets);
	public static final Registry<ClientEvent> CLIENT_EVENTS = create(ClientEvent::registerEvents);
	public static final Registry<UserEvent> USER_EVENTS = create(UserEvent::registerEvents);
	public static final Registry<Request> REQUESTS = create(Request::registerRequests);
	public static final Registry<Response> RESPONSES = create(Response::registerResponses);
	
	public static <T> Registry<T> create(Consumer<Registry<T>> initializer) {
		Registry<T> registry = new Registry<>();
		initializer.accept(registry);
		return registry;
	}
	
	public static <T> void register(Registry<T> registry, String id, Class<? extends T> clazz) {
		registry.register(id, clazz);
	}
	
	public static <T> String getId(Registry<T> registry, Class<? extends T> clazz) {
		return registry.getId(clazz);
	}
	
	public static <T> Class<? extends T> getClazz(Registry<T> registry, String id) {
		return registry.getClazz(id);
	}
}
