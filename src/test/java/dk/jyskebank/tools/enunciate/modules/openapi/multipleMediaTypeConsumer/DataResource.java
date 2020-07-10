package dk.jyskebank.tools.enunciate.modules.openapi.multipleMediaTypeConsumer;

import com.webcohesion.enunciate.metadata.rs.ResourceGroup;
import com.webcohesion.enunciate.metadata.rs.TypeHint;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ResourceGroup("Resource group text")
@Path("/path/{pathArg}")
public class DataResource {
	/**
	 * Summary.
	 *
	 * Description follows...
	 *
	 * @param pathArg Argument on path.
	 */
	@PUT
	@TypeHint(DataDTO.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
	public Response update(@PathParam("pathArg") String pathArg, DataDTO customerDTO) {
		return Response.ok(new DataDTO()).build();
	}

}
