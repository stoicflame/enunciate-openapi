package dk.jyskebank.tools.enunciate.modules.openapi.oneof;

import com.webcohesion.enunciate.metadata.rs.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ResourceGroup("Data")
@RequestHeaders({ // Common headers
	@RequestHeader(name = "userid", description = "The actual user performing the operation") 
})
@StatusCodes({ // Common response codes
	@ResponseCode(code = 400, condition = "Bad request, inspect response payload for error details"),
	@ResponseCode(code = 401, condition = "Unauthorized, please provide valid Authentication header"),
	@ResponseCode(code = 403, condition = "Forbidden, user does not have permission"),
	@ResponseCode(code = 404, condition = "Not Found, inspect response for more info"),
	@ResponseCode(code = 500, condition = "Internal Server Error, Unexpected server error")
})

@Path("/data/{pathArg}")
public class DataResource {

	/**
	 * Summary.
	 *
	 * Description follows...
	 *
	 * @param pathArg
	 *            The id of the customer
	 */
	@StatusCodes({
			@ResponseCode(code = 201, condition = "Created, Consents created", type=@TypeHint(dk.jyskebank.tools.enunciate.modules.openapi.oneof.DataDTO.class)),
			@ResponseCode(code = 204, condition = "No Content, Current consents updated")
	})

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("pathArg") String pathArg, @NotNull(message = "Mandatory consents payload is missing") @Valid DataDTO customerDTO) {
		return Response.ok(new DataDTO()).build();
	}

}
