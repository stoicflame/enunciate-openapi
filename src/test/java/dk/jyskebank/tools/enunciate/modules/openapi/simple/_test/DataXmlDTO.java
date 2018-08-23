package dk.jyskebank.tools.enunciate.modules.openapi.simple._test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="dataXml")
@XmlType(propOrder = { "first", "content", "password", "receivers", "last" })
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
	/** Last string. Should be last in serialization order. */
	private String last;
	/** First string. Should be first in serialization order. */
	private String first;
	
	@XmlElement(name="password")
	private String password;

	@XmlElement(name="content")
	private byte[] content;

	@XmlElementWrapper(name="receivers")
	@XmlElement(name = "receiver")
	private List<String> receivers = new ArrayList<>();


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
