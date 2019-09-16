package dk.jyskebank.tools.enunciate.modules.openapi.jackson1;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("madness")
@JsonPropertyOrder({ "first", "last", })
public class PropertyOrderingDto {
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
