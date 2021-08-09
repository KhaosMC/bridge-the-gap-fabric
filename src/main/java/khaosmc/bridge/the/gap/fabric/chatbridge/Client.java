package khaosmc.bridge.the.gap.fabric.chatbridge;

public class Client {
	
	public String type;
	public String name;
	public String display_color; // RRGGBB
	
	public Client(String type, String name, String display_color) {
		this.type = type;
		this.name = name;
		this.display_color = display_color;
	}
	
	public Client(String type, String name, int color) {
		this(type, name, String.valueOf(color));
	}
	
	public int getColor() {
		try {
			return Integer.parseInt(display_color, 16);
		} catch (NumberFormatException e) {
			return 0xFFFFFF;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Client) {
			Client other = (Client)o;
			return type.equals(other.type) && name.equals(other.name);
		}
		
		return false;
	}
}
