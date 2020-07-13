package dk.jyskebank.tools.enunciate.modules.openapi.multipleMediaTypeProducer;

import com.webcohesion.enunciate.metadata.rs.ResourceGroup;
import com.webcohesion.enunciate.metadata.rs.ResponseCode;
import com.webcohesion.enunciate.metadata.rs.StatusCodes;
import com.webcohesion.enunciate.metadata.rs.TypeHint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ResourceGroup("Resource group text")
@Path("/path/{pathArg}")
public class DataResource {
	/**
	 * An initial summary.
	 *
	 * Followed by a longer description.

     * <p>And some HTML</p>
	 * <pre>
	 * {
	 *  "weird" : "stuff"
	 * }
	 * </pre>
	 *
	 * @param pathArg Argument on path.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@StatusCodes({
			@ResponseCode(code = 200, condition = "OK", type=@TypeHint(DataDTO.class)),
			@ResponseCode(code = 400, condition = "Bad request")
	})
	public Response get(@PathParam("pathArg") String pathArg) {
		return Response.ok(new DataDTO()).build();
	}
}
