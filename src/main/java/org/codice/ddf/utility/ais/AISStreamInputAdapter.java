/**
 * Reads AIS Sentences from an input stream and posts them to and http endpoint.  It will read message fragments and
 * post only when the message is complete.
 */

package org.codice.ddf.utility.ais;




import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AISStreamInputAdapter {

  private static final Logger log = Logger.getLogger(AISStreamInputAdapter.class);

  private static final String CONTENT_TYPE = "application/ais-nmea";
  private InputStream inputStream;

  public AISStreamInputAdapter(InputStream inputStream) {
    log.debug("Creating AISStreamInputAdapter");
    this.inputStream = inputStream;
  }

  public void post(URL url) throws URISyntaxException, IOException, UnhandledMessageException {
    post(url, 0);
  }

  public void post(URL url, long delay) throws URISyntaxException, IOException, UnhandledMessageException{
    HttpClient http = getHttpClient();
    HttpPost post = new HttpPost(url.toURI());

    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(this.inputStream));
    while(bufferedReader.ready()){
      String message = this.getMessage(bufferedReader);
      StringEntity entity = new StringEntity(message, HTTP.UTF_8);

      post.setHeader("Content-Type", CONTENT_TYPE);
      post.setEntity(entity);

      log.debug("Posting message " + message + " to " + url.toString());
      http.execute(post);
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {}
    }
  }

  HttpClient getHttpClient(){
    return new DefaultHttpClient();
  }

  private String [] getTokens(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    if(line == null)
      return null;
    String [] tokens = line.split(",");
    return tokens;
  }

  private String getMessage(BufferedReader reader) throws IOException, UnhandledMessageException {
    List<String> message = new ArrayList<String>();

    String [] tokens = getTokens(reader);

    if(tokens != null && tokens[0].contains("AIVDM")) {
      message.add(StringUtils.join(tokens, ","));
      while(!tokens[1].equals(tokens[2])){
        tokens = getTokens(reader);
        message.add(StringUtils.join(tokens, ","));
      }
    } else {
      throw new UnhandledMessageException("Message Type " + tokens[0] + " not supported.");
    }

    return StringUtils.join(message.toArray(), "\n");
  }
}
