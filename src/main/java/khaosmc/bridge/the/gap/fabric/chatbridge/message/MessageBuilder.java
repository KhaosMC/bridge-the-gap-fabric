package khaosmc.bridge.the.gap.fabric.chatbridge.message;

public class MessageBuilder {
	
	private String type;
	private MessageClient client;
	private MessageAuthor author;
	private String content;
	
	public MessageBuilder() {
		this.type = "";
		this.client = new MessageClient("", "");
		this.author = new MessageAuthor("", 0);
		this.content = "";
	}
	
	public Message build() {
		return new Message(type, client, author, content);
	}
	
	public MessageBuilder setType(String type) {
		this.type = type;
		return this;
	}
	
	public MessageBuilder setClient(String type, String name) {
		return setClient(new MessageClient(type, name));
	}
	
	public MessageBuilder setClient(MessageClient client) {
		this.client = client;
		return this;
	}
	
	public MessageBuilder setAuthor(String name, int displayColor) {
		return setAuthor(new MessageAuthor(name, displayColor));
	}
	
	public MessageBuilder setAuthor(MessageAuthor author) {
		this.author = author;
		return this;
	}
	
	public MessageBuilder setContent(String content) {
		this.content = content;
		return this;
	}
}
