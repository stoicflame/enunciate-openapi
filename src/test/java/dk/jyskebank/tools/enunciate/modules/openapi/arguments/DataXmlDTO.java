package dk.jyskebank.tools.enunciate.modules.openapi.arguments;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataXmlDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
	@XmlElement
	private String simpleStr;
}
