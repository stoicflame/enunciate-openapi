package dk.jyskebank.tools.enunciate.modules.openapi.enumeration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="xmlRootElementRoleType")
@XmlType(name="xmlTypeRoleType")
@XmlEnum
public enum RootElementEnum {
	ADMIN,
	MONITOR
}
