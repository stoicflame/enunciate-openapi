package dk.jyskebank.tools.enunciate.modules.openapi.simple;

import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

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

	@Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

}
