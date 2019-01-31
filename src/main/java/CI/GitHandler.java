package CI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.util.*;
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GitHandler{
  private GitEvent G; // GitEvent object containing all information about the event.

  GitHandler(GitEvent G){
    this.G = G;
  }

  /**
  * Handles an push event by calling all individual functions handling a step each.
  * Meaning, pull, build, test and notify.
  */
  public void request_push(){
        // TODO
      // Called by request_push
      // Try and pull the branch
      // Try and build the branch
      // Execute the Test suite.
      // Send notification
  }

  /**
  * Pulls the branch provided by a GitEvent object.
  * @return - The path to the pulled branch.
  */
  public String pull_branch(){
    // TODO
    return "";
  }

  /**
  * Executes the automated tests
  * @param path - The local path to the script starting the tests
  * @return - True if tests went through, false if not.
  */
  public boolean start_tests(String path){
    // send notification about the results
    // TODO
    return false;
  }

  /**
  * Attempts to build the pulled branch locally
  * @param path - The path to where the cloned branch is located
  * @param buildIndex - the unique build index for the build
  * @return - True if build was successful, false if not.
  */
  public boolean build_branch(String path, int buildIndex){
    return false;
  }

  /**
  * Notifies the pusher of the status of certain requests
  * @author Kartal Kaan Bozdoğan
  * @param message - The message to send
  * @return - True if message was send, false if not.
  */
  public boolean send_notification(String message){
    String from = "top-tier-ci@gmail.com";
    String host = "localhost";

    Properties properties = System.getProperties();
    properties.setProperty("smtp.kth.se", host);
    Session session = Session.getDefaultInstance(properties);  
      
    try{  
        MimeMessage mimeMessage = new MimeMessage(session);  
        mimeMessage.setFrom(new InternetAddress(from));  
        mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(G.pusherEmail));  
        mimeMessage.setSubject("CI Message");  
        mimeMessage.setText(message);

        Transport.send(mimeMessage);  
        return true;
      
    }catch (MessagingException mex) {
        mex.printStackTrace();
        return false;
    }
  }
}
