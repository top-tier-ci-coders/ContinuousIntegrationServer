package CI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GitHandler{


  /**
  * Handles
  * @param G - GitEvent object containing all information about the event.
  */
  public static void request_push(GitEvent G){
              // TODO
            // Called by request_push
            // Try and pull the branch
            // Try and build the branch
            // Execute the Test suite.
            // Send notification
  }

  /**
  * Pulls the branch provided by a GitEvent object.
  * @param G - GitEvent object containing all information about the event.
  */
  public static void pull_branch(GitEvent G){
    // TODO
  }

  /**
  * Executes the automated tests
  * @param G - GitEvent object containing all information about the event.
  */
  public static void start_tests(GitEvent G){
    // send notification about the results
    // TODO
  }

  /**
  * Attempts to build the pulled branch locally
  * @param G - GitEvent object containing all information about the event.  
  */
  public static void build_branch(GitEvent G){

  }

  /**
  * Notifies collaborators of the status of certain requests
  * @param G - GitEvent object containing all information about the event.
  */
  public static void send_notification(GitEvent G){
              // TODO
  }


}
