package dk.jyskebank.tools.enunciate.modules.openapi.validation;

import javax.validation.constraints.Pattern;

public class PatternDto {
	private String data;

	@Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
	public String getFirst() {
		return data;
	}

	public void setFirst(String data) {
		this.data = data;
	}

}
