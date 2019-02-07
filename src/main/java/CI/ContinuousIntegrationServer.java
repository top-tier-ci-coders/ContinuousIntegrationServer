package CI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.Enumeration;
import java.lang.Exception;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import org.json.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/** 
 *  ContinuousIntegrationServer which acts as a webhook and
 *  stores the history of builds.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{

	  /**
    *  Handles three different types of HTTP request:
    *  - target = "/" and header X-GitHub-Event = "push"
    *      This request will pull the branch mentioned in the payload,
    *      build it, run its tests, email the results of the build
    *      and tests to the one that made the push request,
    *      and then store a log of the results.
    *  - target = "/list"
    *      Return a list of all build ids.
    *  - target = "/<build id>"
    *      Return the results of the specified build.
	  * @author Kartal Kaan Bozdoğan and Marcus Östling
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

        if (target.equals("/list")) {
            // List ids of all build
            System.out.println("LIST all build request.");
            String[] builds = BuildLogger.listBuilds();
            if(builds.length > 0) {
              String allBuilds = "List of build ids:<br>";
              for(String s : builds) {
                allBuilds += s + "<br>";
              }
              try {
                  response.getWriter().println(allBuilds);
              } catch (IOException ioe) {
                  ioe.printStackTrace();
              }
            } else {
              try {
                  response.getWriter().println("No build logs found.");
              } catch (IOException ioe) {
                  ioe.printStackTrace();
              }
            }

        } else if(Pattern.compile("^/[0-9]+$").matcher(target).matches()){
            // List a unique build
            System.out.println("List build:" + target.substring(1) + " request.");
            String status = BuildLogger.getBuildStatus(target.substring(1));
            if(status != null) {
                try {
                    response.getWriter().println(status);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else {
                try {
                    response.getWriter().println("Invalid build id.");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        } else {
            // Handle github webhook push event
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
                catch (IOException | JSONException ex) {
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
