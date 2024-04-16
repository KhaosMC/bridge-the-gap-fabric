package khaosmc.bridge.the.gap.chatbridge;

public enum Platform {

	MINECRAFT,
	DISCORD;

	public int id() {
		return ordinal();
	}

	public static Platform byId(int id) {
		Platform[] values = values();

		if (id < 0 || id >= values.length) {
			throw new IllegalArgumentException("invalid platform id " + id);
		}

		return values[id];
	}
}
