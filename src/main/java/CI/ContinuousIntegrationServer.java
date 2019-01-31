package CI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import java.util.Enumeration;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/** 
 *  Skeleton of a ContinuousIntegrationServer which acts as webhook
 *   See the Jetty documentation for API documentation of those classes.
 *   */
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) 
            throws IOException, ServletException
        {
            String eventType = "";
            for (Enumeration<String> e = request.getHeaders("X-GitHub-Event"); e.hasMoreElements();) {
                eventType = e.nextElement();
            }

			String jsonStr = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			GitEvent gitEvent = new GitEvent(eventType, jsonStr);
			
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            // here you do all the continuous integration tasks
            // for example
            // 1st clone your repository
            // 2nd compile the code

            response.getWriter().println("CI job done");
        }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
