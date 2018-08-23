package dk.jyskebank.tools.enunciate.modules.openapi.nillable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataXmlDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
	@XmlElement(nillable=false)
	private boolean success = true;

	@XmlElement(nillable=true)
	private Boolean nillableBool;

	@XmlElement(nillable=false)
	private Boolean requiredBool;

	@XmlElement(nillable=true)
	private String nillableStr;

	@XmlElement(nillable=false)
	private String requiredStr;
	
	@XmlElement(nillable=false)
	private DataXmlEnum requiredEnum;

	@XmlElement(nillable=true)
	private DataXmlEnum nillableEnum;

	@XmlElement(nillable=false)
	private SubDTO requiredDto;

	@XmlElement(nillable=true)
	private SubDTO nillableDto;
}
