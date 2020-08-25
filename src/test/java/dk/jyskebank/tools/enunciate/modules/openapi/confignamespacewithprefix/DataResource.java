package dk.jyskebank.tools.enunciate.modules.openapi.confignamespacewithprefix;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

@Path("/path")
public class DataResource {

	@TypeHint(DataXmlDTO.class)
	@GET
	@Path("/xmldto")
	@Produces(MediaType.APPLICATION_XML)
    public Response getXmlType() {
      return Response.ok(new DataXmlDTO()).build();
	}
}
