package khaosmc.bridge.the.gap.chatbridge;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import khaosmc.bridge.the.gap.BridgeTheGapMod;
import khaosmc.bridge.the.gap.chatbridge.packet.request.*;
import khaosmc.bridge.the.gap.chatbridge.packet.response.*;

import net.minecraft.server.MinecraftServer;

public class RequestHandler {

	private final BridgeTheGapClient chatBridge;
	private final MinecraftServer server;
	private final PacketHandler packetHandler;
	private final Int2ObjectMap<String> requests;

	private byte nextTransaction;

	public RequestHandler(BridgeTheGapClient chatBridge, MinecraftServer server, PacketHandler packetHandler) {
		this.chatBridge = chatBridge;
		this.server = server;
		this.packetHandler = packetHandler;
		this.requests = new Int2ObjectOpenHashMap<>();
	}
}
