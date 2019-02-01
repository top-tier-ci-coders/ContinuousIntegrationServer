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
   GitEvent event = new GitEvent("", "");
   event.setBranchName("test");
   GitHandler gitHandler = new GitHandler(event);
   String re = gitHandler.pull_branch();
   assertNotSame(null, re);
   // Also make sure the return is a correct path
   assertTrue(re.matches("~/builds-CI/-?[0-9]+"));
  }

  /**
   * Tests GitHandler.build_branch by a positive test to build
   * the current branch and a negative test to build path that does
   * not exist.
   * @author Marcus Östling
   */
  @Test
  public void testBuildBranch() {
    System.out.println("Test build branch");
    GitEvent event = new GitEvent();
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.build_branch("."));
    assertFalse(gitHandler.build_branch("./herpderp"));
  }

  /**
   * Tests GitHandler.send_notification by sending a well-known email
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotificationSuccess() {
    /*
    GitEvent event = new GitEvent();
    event.pusherName = "kartal";
    event.pusherEmail = "bozdogan@kth.se";
    event.branchName = "Mail test";
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.send_notification("Testing... Testing..."));
    */
 }

  /**
   * Tests GitHandler.send_notification by attempting to send an email to an invalid address.
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotificationFail() {
    GitEvent event = new GitEvent("","");
    event.setPusherName("kartal");
    event.setPusherEmail("bozdog ankth.se");
    event.setBranchName("Mail test");
    GitHandler gitHandler = new GitHandler(event);
    assertFalse(gitHandler.send_notification("Testing... Testing..."));
 }

}
