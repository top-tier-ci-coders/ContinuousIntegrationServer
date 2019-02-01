package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GitHandlerTest {

  /**
  * Tests that the pull_branch function terminates properly by returning
  * a path that isn't null.
  * @author Andreas Gylling, Philippa Örnell
  */
  @Test
  public void testPullBranch(){
   GitEvent event = new GitEvent();
   event.branchName = "test";
   GitHandler gitHandler = new GitHandler(event);
   String re = gitHandler.pull_branch();
   assertNotSame(null, re);
   // Also make sure the return is a correct path
   assertTrue(re.matches("~/builds-CI/-?[0-9]+"));
  }

  /**
   * Tests GitHandler.send_notification by sending a well-known email
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotification() {
    /*
    GitEvent event = new GitEvent();
    event.pusherName = "kartal";
    event.pusherEmail = "bozdogan@kth.se";
    event.branchName = "Mail test";
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.send_notification("Testing... Testing..."));
    */
 }
}
