package khaosmc.bridge.the.gap.fabric.chatbridge.packet;

import khaosmc.bridge.the.gap.fabric.chatbridge.packet.c2s.*;
import khaosmc.bridge.the.gap.fabric.chatbridge.packet.s2c.*;
import khaosmc.bridge.the.gap.fabric.util.Registry;

public class Packets {
	
	private static final Registry<C2SPacket> C2S;
	private static final Registry<S2CPacket> S2C;
	
	static {
		
		C2S = new Registry<>();
		S2C = new Registry<>();
		
		registerC2S("auth"             , AuthC2SPacket.class);
		registerC2S("user_connection"  , UserConnectionC2SPacket.class);
		registerC2S("chat_message"     , ChatMessageC2SPacket.class);
		registerC2S("client_message"   , ClientMessageC2SPacket.class);
		
		registerS2C("auth"             , AuthS2CPacket.class);
		registerS2C("client_connection", ClientConnectionS2CPacket.class);
		registerS2C("user_connection"  , UserConnectionS2CPacket.class);
		registerS2C("chat_message"     , ChatMessageS2CPacket.class);
		registerS2C("client_message"   , ClientMessageS2CPacket.class);
		
	}
	
	public static void registerC2S(String id, Class<? extends C2SPacket> clazz) {
		C2S.register(id, clazz);
	}
	
	public static void registerS2C(String id, Class<? extends S2CPacket> clazz) {
		S2C.register(id, clazz);
	}
	
	public static Class<? extends C2SPacket> getClazzC2S(String type) {
		return C2S.getClazz(type);
	}
	
	public static String getTypeC2S(C2SPacket packet) {
		return C2S.getId(packet.getClass());
	}
	
	public static Class<? extends S2CPacket> getClazzS2C(String type) {
		return S2C.getClazz(type);
	}
	
	public static String getTypeS2C(S2CPacket packet) {
		return S2C.getId(packet.getClass());
	}
}
