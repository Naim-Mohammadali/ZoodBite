import config.AppConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server server = new Server(port);
        server.setHandler(context);

        ServletContainer container = new ServletContainer(new AppConfig());
        ServletHolder jerseyServlet = new ServletHolder(container);
        context.addServlet(jerseyServlet, "/*");

        server.start();
        System.out.println("✅ Server started at http://localhost:" + port);
        server.join();
    }
}