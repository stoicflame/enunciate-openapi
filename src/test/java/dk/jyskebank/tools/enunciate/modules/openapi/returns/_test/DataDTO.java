package dk.jyskebank.tools.enunciate.modules.openapi.returns._test;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("madness")
@JsonPropertyOrder({ "first", "last", })
public class DataDTO {
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
