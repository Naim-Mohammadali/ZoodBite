package rest;

import dto.user.request.UserLoginRequest;
import dto.user.response.AuthResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import service.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService auth = new AuthService();

    @POST @Path("/login")
    public Response login(UserLoginRequest dto) {
        AuthResponse resp = auth.login(dto);
        return Response.ok(resp).build();
    }
}
