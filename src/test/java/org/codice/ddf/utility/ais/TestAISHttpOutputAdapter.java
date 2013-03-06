package org.codice.ddf.utility.ais;

import junit.framework.TestCase;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: kwplummer
 * Date: 3/6/13
 * Time: 10:43 AM
 */
public class TestAISHttpOutputAdapter extends TestCase {


  @Test()
  public void testPostingSingleLineMessage() throws IOException {
    String string = "!AIVDM,1,1,,B,B3=9=R000089kSV?44k5wwWUkP06,0*0E,rORBCOMM000u,1361899938,1361899956";

    final HttpClient client = mock(HttpClient.class);
    when(
            client.execute((HttpHost)notNull(), (HttpRequest)any())
    ).thenReturn(null);
    AISHttpOutputAdapter adapter = new AISHttpOutputAdapter(new ByteArrayInputStream(string.getBytes())){
      @Override
      protected HttpClient getHttpClient() {
        return client;
      }
    };

    try {
      adapter.post(new URL("http://localhost:8181/services/catalog"));
    } catch (URISyntaxException e) {
       fail(e.getMessage());
    } catch (UnhandledMessageException e) {
      fail(e.getMessage());
    }
  }


  @Test()
  public void testPostingMultiLineMessage() throws IOException {
    String string = "!AIVDM,2,1,5,B,53=Go2027sjOTP7;O3LmJ0=<j1A8TlpE>2222216?`I=;4eg0EQC3QDmPAiC,0*6A,rORBCOMM000,1361899938\n" +
    "!AIVDM,2,2,5,B,`8888888880,2*7A,rORBCOMM000,1361899938,1361899956";

    final HttpClient client = mock(HttpClient.class);
    when(
            client.execute((HttpHost)notNull(), (HttpRequest)any())
    ).thenReturn(null);
    AISHttpOutputAdapter adapter = new AISHttpOutputAdapter(new ByteArrayInputStream(string.getBytes())){
      @Override
      protected HttpClient getHttpClient() {
        return client;
      }
    };

    try {
      adapter.post(new URL("http://localhost:8181/services/catalog"));
    } catch (URISyntaxException e) {
      fail(e.getMessage());
    } catch (UnhandledMessageException e) {
      fail(e.getMessage());
    }
  }
}
