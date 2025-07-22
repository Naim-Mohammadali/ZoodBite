package controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/openapi.yaml")
public class SwaggerController {

    @GET
    @Produces("application/yaml")
    public InputStream serveOpenApi() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("openapi.yaml");
        if (stream == null) {
            throw new WebApplicationException("OpenAPI spec not found", 404);
        }
        return stream;
    }
}
