package dk.jyskebank.tools.enunciate.modules.openapi.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * duplet property has too many annotations.
 */
public class DupletAnnotationsDto {
	@JsonProperty("Duplet")
	private String duplet;
	@JsonProperty("Valid")
	private String valid;
	private String alsoValid;
	
	@JsonProperty("Duplet")
	public String getDuplet() {
		return duplet;
	}

	@JsonProperty("Duplet")
	public void setDuplet(String duplet) {
		this.duplet = duplet;
	}
	
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}

	@JsonProperty("AlsoValid")
	public String getAlsoValid() {
		return alsoValid;
	}

	@JsonProperty("AlsoValid")
	public void setAlsoValid(String alsoValid) {
		this.alsoValid = alsoValid;
	}
}
