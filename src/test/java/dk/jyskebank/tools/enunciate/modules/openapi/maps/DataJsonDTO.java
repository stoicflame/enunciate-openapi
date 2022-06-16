package dk.jyskebank.tools.enunciate.modules.openapi.maps;

import java.util.Map;

/**
 * Simple generic data container to check valid openapi.yml for additionalProperties (should be an empty object!).
 *
 * @author FabianHalbmann
 */
public class DataJsonDTO {

	private Map<String, Object> data;

	/**
	 * A very generic data container.
	 */
	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
