package server;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import rest.security.JwtFilter;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

import java.net.URI;

public class RestServer {
    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig()
                .packages("rest")
                .register(JwtFilter.class)
                .register(OpenApiResource.class)
                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        var server = JettyHttpContainerFactory
                .createServer(URI.create("http://0.0.0.0:8080/"), rc);

        server.start();
        System.out.println("🟢  REST on http://localhost:8080");
        System.out.println("🟢  Swagger UI at /swagger-ui/index.html");
        server.join();
    }
}
