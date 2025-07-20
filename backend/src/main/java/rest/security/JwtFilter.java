// rest/security/JwtFilter.java
package rest.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;
import model.User;
import service.UserService;
import util.JwtUtil;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    private final UserService users = new UserService();

    @Override
    public void filter(ContainerRequestContext ctx) {
        // let /auth/* go through
        if (ctx.getUriInfo().getPath().startsWith("auth")) return;

        String hdr = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (hdr == null || !hdr.startsWith("Bearer ")) {
            abort(ctx, 401, "Missing token");
            return;
        }
        try {
            var claims = JwtUtil.parse(hdr.substring(7)).getPayload();
            long id = Long.parseLong(claims.getSubject());
            var user = users.findById(id);

            if (user.getStatus() == User.Status.BLOCKED)
                abort(ctx, 403, "Blocked account");

            ctx.setProperty("user", user);  // downstream can use it
        } catch (Exception e) {
            abort(ctx, 401, "Invalid or expired token");
        }
    }

    private void abort(ContainerRequestContext ctx, int code, String msg) {
        ctx.abortWith(Response.status(code).entity(msg).build());
    }
}
