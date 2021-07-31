package khaosmc.bridge.the.gap.fabric.chatbridge.message;

public class Message {
	
	public final String type;
	public final MessageClient client;
	public final MessageAuthor author;
	public final String content;
	
	public Message(String type, MessageClient client, MessageAuthor author, String content) {
		this.type = type;
		this.client = client;
		this.author = author;
		this.content = content;
	}
}
