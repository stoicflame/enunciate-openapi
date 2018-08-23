package dk.jyskebank.tools.enunciate.modules.openapi.pkgnamespace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataXml")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
	@XmlElement(name="aString")
	private String aString;
	
	@XmlElement
	private DataXmlEnum anEnum;
}
