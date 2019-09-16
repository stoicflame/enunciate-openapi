package dk.jyskebank.tools.enunciate.modules.openapi.jackson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.ResourceGroup;
import com.webcohesion.enunciate.metadata.rs.TypeHint;


@ResourceGroup("Data")
@Path("/jackson")
public class Resource {
	@Path("/ordering")
	@TypeHint(PropertyOrderingDto.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		return Response.ok(new PropertyOrderingDto()).build();
	}

	@Path("/ignore")
	@TypeHint(IgnoreMethodsDto.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIgnore() {
		return Response.ok(new IgnoreMethodsDto()).build();
	}

	@Path("/bad-duplets")
	@TypeHint(DupletAnnotationsDto.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDuplet() {
		return Response.ok(new DupletAnnotationsDto()).build();
	}
}
