package dk.jyskebank.tools.enunciate.modules.openapi.multipleMediaTypeConsumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="data")
@XmlType(propOrder = { "first", "last"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DataDTO {
	private String first;
	private String last;

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
