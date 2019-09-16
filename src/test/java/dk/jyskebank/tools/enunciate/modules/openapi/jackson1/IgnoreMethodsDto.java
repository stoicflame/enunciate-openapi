package dk.jyskebank.tools.enunciate.modules.openapi.jackson1;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Test that @JsonIgnore is respected.
 */
public class IgnoreMethodsDto {
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return data != null && data.isEmpty();
	}

	@JsonIgnore
	public boolean isNotEmpty() {
		return !isEmpty();
	}
}
