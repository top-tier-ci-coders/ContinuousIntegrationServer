package CI;

import org.junit.Test;
import static org.junit.Assert.*;

public class GitHandlerTest {
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
