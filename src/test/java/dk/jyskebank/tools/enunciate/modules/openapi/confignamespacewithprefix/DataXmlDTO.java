package dk.jyskebank.tools.enunciate.modules.openapi.confignamespacewithprefix;

import javax.xml.bind.annotation.*;

@XmlType(name = "dataXmlDTO", namespace = "http://www.jyskebank.dk/enunciate/openapi/version/1")
@XmlRootElement(name = "dataXmlDTO", namespace = "http://www.jyskebank.dk/enunciate/openapi/version/1")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
    @XmlElement(name = "aString")
    private String aString;

    @XmlElement
    private DataXmlEnum anEnum;

    @XmlElement(name = "dataValue", namespace = "http://www.jyskebank.dk/enunciate/openapi/version/2")
    private DataXmlDTODataValue dataValue;
}
