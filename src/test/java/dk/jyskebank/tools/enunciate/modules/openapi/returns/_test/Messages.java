package dk.jyskebank.tools.enunciate.modules.openapi.returns._test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

@XmlRootElement
public class Messages {
	
	private List<String> messages = new ArrayList<String>();
	
	public List<String> getMessages() {
		return messages;
	}
	
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void add(String newMessage) {
		if (messages.stream().anyMatch(m -> m.equals(newMessage))) {
			return;
		}
		this.messages.add(newMessage);
	}

	@JsonIgnore
	public boolean isNotOk() {
		return messages.stream().anyMatch(m -> "error".equals(m));
	}

	@JsonIgnore
	public boolean isOK() {
		return !isNotOk();
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this).toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
