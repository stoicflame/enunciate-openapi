package dk.jyskebank.tools.enunciate.modules.openapi.enumeration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

@Path("/path")
@Produces({ MediaType.APPLICATION_XML})
@Consumes({ MediaType.APPLICATION_XML})
public class DataResource {
	@TypeHint(RootElementEnum.class)
	@GET
	@Path("/root")
	public Response getRootElementEnum() {
		return Response.ok(RootElementEnum.ADMIN).build();
	}

	@Path("/ref")
	@TypeHint(DataXmlDTO.class)
	@GET
	public Response getReferenceEnum() {
		return Response.ok(new DataXmlDTO()).build();
	}
}
