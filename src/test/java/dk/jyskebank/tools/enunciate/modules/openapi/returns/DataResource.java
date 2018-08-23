package dk.jyskebank.tools.enunciate.modules.openapi.returns;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.RequestHeader;
import com.webcohesion.enunciate.metadata.rs.RequestHeaders;
import com.webcohesion.enunciate.metadata.rs.ResourceGroup;
import com.webcohesion.enunciate.metadata.rs.ResponseCode;
import com.webcohesion.enunciate.metadata.rs.StatusCodes;
import com.webcohesion.enunciate.metadata.rs.TypeHint;


@ResourceGroup("Data")
@RequestHeaders({ // Common headers
	@RequestHeader(name = "userid", description = "The actual user performing the operation") 
})
@StatusCodes({ // Common response codes
	@ResponseCode(code = 400, condition = "Bad request, inspect response payload for error details", type=@TypeHint(Messages.class)), 
	@ResponseCode(code = 401, condition = "Unauthorized, please provide valid Authentication header", type=@TypeHint(Messages.class)), 
	@ResponseCode(code = 403, condition = "Forbidden, user does not have permission", type=@TypeHint(Messages.class)),
	@ResponseCode(code = 404, condition = "Not Found, inspect response for more info", type=@TypeHint(Messages.class)),
	@ResponseCode(code = 500, condition = "Internal Server Error, Unexpected server error", type=@TypeHint(Messages.class))
})
@Path("/data/{pathArg}")
public class DataResource {
	/**
	 * An initial summary.
	 */
	@TypeHint(DataDTO.class)
	@StatusCodes({ 
		   @ResponseCode(code = 200, condition = "Success, Response contains consents") 
	})
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("pathArg") String pathArg) {
		return Response.ok(new DataDTO()).build();
	}


	/**
	 * Summary.
	 * 
	 * Description follows...
	 * 
	 * @param pathArg
	 *            The id of the customer
	 */
	@StatusCodes({ 
		   @ResponseCode(code = 201, condition = "Created, Consents created", type=@TypeHint(DataDTO.class)), 
		   @ResponseCode(code = 204, condition = "No Content, Current consents updated") 
	})

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("pathArg") String pathArg, @NotNull(message = "Mandatory consents payload is missing") @Valid DataDTO customerDTO) {
		return Response.ok(new DataDTO()).build();
	}

	/**
	 * Summary delete.
	 * 
	 * Description delete.
	 *
	 * @param pathArg
	 *            The id of the customer
	 * 
	 */
	@StatusCodes({ 
		@ResponseCode(code = 200, condition = "Success, consents deleted", type=@TypeHint(Messages.class)) 
	})
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("pathArg") String pathArg) {
		Messages result = new Messages();
		return Response.ok(result).build();
	}
	
	@Path("/bytearray-input")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("pathArg") String path, byte[] body) {
	  return Response.ok("").build();
	}
}
