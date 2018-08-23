package dk.jyskebank.tools.enunciate.modules.openapi.nillable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subDto")

public class SubDTO {
	/** Last string. Should be last in serialization order. */
	private String last;
	/** First string. Should be first in serialization order. */
	private String first;

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

}
