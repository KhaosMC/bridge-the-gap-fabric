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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Client) {
			Client other = (Client)o;
			return name.equals(other.name);
		}
		
		return false;
	}
	
	public Integer getColor() {
		try {
			return Integer.parseInt(display_color, 16);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
