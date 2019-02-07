package CI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.Enumeration;
import java.lang.Exception;
import java.io.IOException;
import java.util.stream.Collectors;

import org.json.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/** 
 *  Skeleton of a ContinuousIntegrationServer which acts as webhook
 *   See the Jetty documentation for API documentation of those classes.
 *   */
public class ContinuousIntegrationServer extends AbstractHandler
{

	/**
    * Handles HTTP requests. Called by jetty.
	* @author Kartal Kaan Bozdoğan
    * @param target - The target URL
	* @param baseRequest - The base request, as set by jetty
	* @param request - The http request
	* @param response - The http response
    */
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) 
        {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            Enumeration<String> e = request.getHeaders("X-GitHub-Event");
            if (e.hasMoreElements() == false)
            {
                System.out.println("ERROR: The received request has no X-GitHub-Event header.");
                try {
                    response.getWriter().println("Invalid request");
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return ;
            }
            String eventType = e.nextElement();
            if (eventType.equals("push"))
            {
		try {
                    GitEvent event = new GitEvent("push",
			request.getReader().lines().collect(
				Collectors.joining(System.lineSeparator())));
                    GitHandler handler = new GitHandler(event);
                    System.out.println("Received a push event for the branch \"" +
			event.getBranchName() + "\"");
                    handler.request_push();
                    response.getWriter().println("CI job done");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                return ;
            }
            else
            {
		System.out.println("Ignoring event type \"" + eventType + "\"");   
                try {
                    response.getWriter().println("CI job done");
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
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
