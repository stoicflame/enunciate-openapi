package dk.jyskebank.tools.enunciate.modules.openapi.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataXmlDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataXmlDTO {
	@XmlElement
	private Date aDate;

	@XmlElement
	private Calendar aCalendar;

	@XmlElement
	private LocalDate aLocalDate;

	@XmlElement
	private LocalTime aLocalTime;
	
	@XmlElement
	private LocalDateTime aLocalDateTime;

	@XmlElement
	private OffsetTime anOffsetTime;

	@XmlElement
	private OffsetDateTime anOffsetDateTime;

	@XmlElement
	private ZonedDateTime aZonedDateTime;
}
