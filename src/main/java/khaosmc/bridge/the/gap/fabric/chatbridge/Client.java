package khaosmc.bridge.the.gap.fabric.chatbridge;

public class Client {
	
	public String type;
	public String name;
	
	public Client(String type, String name) {
		this.type = type;
		this.name = name;
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
