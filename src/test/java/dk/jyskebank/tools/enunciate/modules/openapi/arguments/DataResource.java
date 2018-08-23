package dk.jyskebank.tools.enunciate.modules.openapi.arguments;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

@Path("/path")
public class DataResource {

  @TypeHint(String.class)
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response noArgs() {
    return dummyResult();
  }

  @TypeHint(String.class)
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("entity")
  public Response withEntity(String entity) {
    return dummyResult();
  }

  @TypeHint(String.class)
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("matrix/{id}")
  public Response withMatrixParams(@MatrixParam("id") List<Long> ids) {
    return dummyResult();
  }

  private Response dummyResult() {
    return Response.ok("OK").build();
  }

}
