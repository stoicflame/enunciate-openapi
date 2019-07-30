package dk.jyskebank.tools.enunciate.modules.openapi.utf8;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;


@Path("/path/{pathArg}")
public class DataResource {
	@TypeHint(DataUTF8CharsetFieldsDTO.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("pathArg") String pathArg) {
		return Response.ok(new DataUTF8CharsetFieldsDTO()).build();
	}
}
