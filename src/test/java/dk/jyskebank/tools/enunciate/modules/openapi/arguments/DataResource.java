package dk.jyskebank.tools.enunciate.modules.openapi.arguments;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
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
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  @Path("stringEntity")
  public Response withStringEntity(String entity) {
    return dummyResult();
  }

  @TypeHint(String.class)
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_XML)
  @Path("dtoEntity")
  public Response withDtoEntity(DataXmlDTO entity) {
    return dummyResult();
  }

  @TypeHint(String.class)
  @GET
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.TEXT_PLAIN)
  @Path("inputStream")
  public Response withInputStream(InputStream is) {
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
