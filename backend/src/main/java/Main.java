import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import rest.security.JwtFilter;

import java.net.URI;

public class Main {
    public static void main(String[] args) {
        ResourceConfig cfg = new ResourceConfig()
                .packages("controller")
                .register(JwtFilter.class)
                .register(new OpenApiResource());
        JettyHttpContainerFactory.createServer(
                URI.create("http://0.0.0.0:8080/"), cfg);
    }
}
