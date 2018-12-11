package dk.jyskebank.tools.enunciate.modules.openapi.nillable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/path")
public class DataResource {
	/* TODO: This is bad - should use org.jboss.resteasy.spi.StringConverter and produces TEXT
	@TypeHint(Date.class)
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/date")
	public Response get() {
		return Response.ok(new Date()).build();
	}
	*/

	/* TODO: This is bad - should use org.jboss.resteasy.spi.StringConverter and consumes TEXT
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces()
	@Path("/date")
	public Response acceptDate(Date data) {
		return Response.ok().build();
	}
	*/

	@GET
	@Path("/xmldto")
	@Produces(MediaType.APPLICATION_XML)
    public DataXmlDTO getXmlType() {
      return new DataXmlDTO();
	}
}
