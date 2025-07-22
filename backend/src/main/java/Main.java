import controller.SwaggerController;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.EnumSet;

public class Main {

    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.packages("controller"); // Auto-register all controllers including Swagger

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        server.start();
        System.out.println("✅ Jetty Server running at: http://localhost:8080");
        server.join();
    }
}
