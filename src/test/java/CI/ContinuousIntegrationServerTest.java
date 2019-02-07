package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.util.HashSet;
import java.util.Arrays;
import java.util.Vector;
import java.io.*;

public class ContinuousIntegrationServerTest {

  /**
  * Tests the handle function's happy path (push on master, it builds and tests properly).
  * @author Kartal Kaan Bozdoğan
  */
  @Test
  public void handleTestHappy(){
    Request baseRequest = mock(Request.class);
    HttpServletRequest request = mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    HttpServletResponse response = mock(HttpServletResponse.class, RETURNS_DEEP_STUBS);
    //PrintWriter writer = mock(PrintWriter.class);
    //doNothing().when(writer).println(anyString());
    //when(response.getWriter()).thenReturn(writer);
    ContinuousIntegrationServer cis = new ContinuousIntegrationServer();
    try {
        when(request.getHeaders("X-GitHub-Event")).thenReturn(
	    new Vector(new HashSet<String>(Arrays.asList("push"))).elements());
        when(request .getReader()).thenReturn(
	    new BufferedReader(new FileReader("./src/test/java/CI/jsonStr")));
    }
    catch (IOException e) {
	e.printStackTrace();
    }
    cis.handle("/", baseRequest, request, response);
    verify(baseRequest).setHandled(true);
    verify(response).setContentType("text/html;charset=utf-8");
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  /**
  * Tests the handle function with an empty request
  * @author Kartal Kaan Bozdoğan
  */
  @Test
  public void handleTestEmptyRequest(){
    Request baseRequest = mock(Request.class);
    HttpServletRequest request = mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    HttpServletResponse response = mock(HttpServletResponse.class, RETURNS_DEEP_STUBS);
    ContinuousIntegrationServer cis = new ContinuousIntegrationServer();
    try {
        when(request.getHeaders("X-GitHub-Event")).thenReturn(
	    new Vector(new HashSet<String>(Arrays.asList())).elements());
        when(request .getReader()).thenReturn(
	    new BufferedReader(new StringReader("")));
    }
    catch (IOException e) {
	e.printStackTrace();
    }
    cis.handle("/", baseRequest, request, response);
    verify(baseRequest).setHandled(true);
    verify(response).setContentType("text/html;charset=utf-8");
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  /**
  * Tests the handle function with a push notification that has an invalid body
  * @author Kartal Kaan Bozdoğan
  */
  /*@Test
  public void handleTestInvalidJSON(){
    Request baseRequest = mock(Request.class);
    HttpServletRequest request = mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    HttpServletResponse response = mock(HttpServletResponse.class, RETURNS_DEEP_STUBS);
    ContinuousIntegrationServer cis = new ContinuousIntegrationServer();
    try {
        when(request.getHeaders("X-GitHub-Event")).thenReturn(
	    new Vector(new HashSet<String>(Arrays.asList("push"))).elements());
        when(request .getReader()).thenReturn(
	    new BufferedReader(new StringReader("[")));
    }
    catch (IOException e) {
	e.printStackTrace();
    }
    cis.handle("/", baseRequest, request, response);
    verify(baseRequest).setHandled(true);
    verify(response).setContentType("text/html;charset=utf-8");
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }*/


}
