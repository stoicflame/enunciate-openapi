package dk.jyskebank.tools.enunciate.modules.openapi.operationid;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;


@Path("/operationid")
public class Resource {
	///////////////////////////////////////////////////
	// The conflict() methods are overloaded without problems in java.
	// But the resulting operationIds still need to be unique.
	//
	// Note that parameter object has no (parameter) name
	@Path("/conflict")
	@TypeHint(String.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response conflict() {
		return Response.ok("").build();
	}
	@Path("/conflict/{id}")
	@TypeHint(String.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response conflict(@PathParam("id") long id) {
		return Response.ok("").build();
	}
	@Path("/conflict/other/{id}")
	@TypeHint(String.class)
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response conflict(@PathParam("id") long id, InputStream is) {
		return Response.ok("").build();
	}
	@Path("/conflict/dto-type/{id}")
	@TypeHint(String.class)
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response conflict(@PathParam("id") long id, String string) {
		return Response.ok("").build();
	}


	// It is possible (but not a good idea) to place multiple http types on same method 
	@Path("/conflict_multi")
	@TypeHint(String.class)
	@PUT
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response conflict_multi() {
		return Response.ok("").build();
	}

}
