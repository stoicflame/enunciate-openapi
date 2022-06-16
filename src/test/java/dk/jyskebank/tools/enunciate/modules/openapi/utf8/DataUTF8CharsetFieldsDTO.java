package dk.jyskebank.tools.enunciate.modules.openapi.utf8;

import org.codehaus.jackson.annotate.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class DataUTF8CharsetFieldsDTO {
	@JsonProperty("fieldContainsDanishLettersÆØÅæøå")
	private Integer withDanishLetters;
	@JsonProperty("noFunkyLettersHere")
	private Integer simpleNamedField;
	
	@JsonProperty("fieldContainsDanishLettersÆØÅæøå")
	public Integer getWithDanishLetters() {
		return withDanishLetters;
	}
	@JsonProperty("fieldContainsDanishLettersÆØÅæøå")
	public void setWithDanishLetters(Integer withDanishLetters) {
		this.withDanishLetters = withDanishLetters;
	}

	@JsonProperty("noFunkyLettersHere")
	public Integer getSimpleNamedField() {
		return simpleNamedField;
	}
	@JsonProperty("noFunkyLettersHere")
	public void setSimpleNamedField(Integer simpleNamedField) {
		this.simpleNamedField = simpleNamedField;
	}
}
