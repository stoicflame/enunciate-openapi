package dk.jyskebank.tools.enunciate.modules.openapi.date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

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

	@TypeHint(DataXmlDTO.class)
	@GET
	@Path("/xmldto")
	@Produces(MediaType.APPLICATION_XML)
    public Response getXmlType() {
      return Response.ok(new DataXmlDTO()).build();
	}
}
