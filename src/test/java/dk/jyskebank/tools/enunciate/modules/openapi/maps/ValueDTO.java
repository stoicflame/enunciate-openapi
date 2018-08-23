package dk.jyskebank.tools.enunciate.modules.openapi.maps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="valueDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValueDTO {
	@XmlElement
	private String somethingOfValue;
}
