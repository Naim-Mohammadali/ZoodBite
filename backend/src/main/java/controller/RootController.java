package controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class RootController {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "ZoodBite API is running!";
    }
}

