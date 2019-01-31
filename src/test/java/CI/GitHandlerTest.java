package CI;

import org.junit.Test;
import static org.junit.Assert.*;

public class GitHandlerTest {
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
