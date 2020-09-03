package dk.jyskebank.tools.enunciate.modules.openapi.confignamespacewithprefix;

import javax.xml.bind.annotation.*;

@XmlType(name = "dataXmlDTODataValue", namespace = "http://www.jyskebank.dk/enunciate/openapi/version/2")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTODataValue {

    @XmlElement(name = "customString")
    private String customString;

}
