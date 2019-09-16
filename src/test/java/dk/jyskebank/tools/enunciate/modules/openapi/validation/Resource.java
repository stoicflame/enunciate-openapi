package dk.jyskebank.tools.enunciate.modules.openapi.validation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;


@Path("/validation")
public class Resource {
	@Path("/pattern")
	@TypeHint(PatternDto.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		return Response.ok(new PatternDto()).build();
	}
}
