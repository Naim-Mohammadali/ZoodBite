package controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/openapi.yaml")
public class SwaggerController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public InputStream serve() {
        return getClass().getClassLoader().getResourceAsStream("openapi.yaml");
    }
}
