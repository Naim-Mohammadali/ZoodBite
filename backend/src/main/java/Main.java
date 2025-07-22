import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // ✅ This works with Jetty 11 + Jakarta Servlet 5.0
        ServletHolder servletHolder = new ServletHolder("jersey", ServletContainer.class);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "controller");

        context.addServlet(servletHolder, "/*");

        server.setHandler(context);
        server.start();
        System.out.println("✅ Server started at http://localhost:8080");
        server.join();
    }
}
