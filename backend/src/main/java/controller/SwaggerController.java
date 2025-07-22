package controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

@Path("/openapi.yaml")
public class SwaggerController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response serveOpenApi() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("openapi.yaml");
        if (stream == null) {
            throw new WebApplicationException("OpenAPI spec not found", 404);
        }

        return Response.ok(stream)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .build();
    }
}
