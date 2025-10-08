package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return; // No auth header; endpoints without @RolesAllowed can proceed
        }

        String token = authHeader.substring("Bearer ".length());
        try {
            Jws<Claims> claims = JwtUtil.parse(token);
            String subject = claims.getPayload().getSubject();
            String role = claims.getPayload().get("role", String.class);

            final SecurityContext current = requestContext.getSecurityContext();
            SecurityContext sc = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> subject;
                }

                @Override
                public boolean isUserInRole(String s) {
                    return role != null && role.equalsIgnoreCase(s);
                }

                @Override
                public boolean isSecure() {
                    return current != null && current.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            };
            requestContext.setSecurityContext(sc);
        } catch (Exception ignored) {
            // Invalid token; let resource method or authorization feature handle access denial
        }
    }
}


