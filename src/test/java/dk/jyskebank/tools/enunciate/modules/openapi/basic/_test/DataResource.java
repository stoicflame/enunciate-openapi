package dk.jyskebank.tools.enunciate.modules.openapi.basic._test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.ResourceGroup;
import com.webcohesion.enunciate.metadata.rs.TypeHint;

@ResourceGroup("Resource group text")
@Path("/path")
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
	@TypeHint(String.class)
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response get() {
		return Response.ok("OK").build();
	}
}
