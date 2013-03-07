/**
 * Reads AIS Sentences from an input stream and posts them to and http endpoint.  It will read message fragments and
 * post only when the message is complete.
 */

package org.codice.ddf.utility.ais;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AISInputStreamAdapter {

  private static final Logger log = Logger.getLogger(AISInputStreamAdapter.class);

  private static final String CONTENT_TYPE = "application/ais-nmea";
  private InputStream inputStream;

  public AISInputStreamAdapter(InputStream inputStream) {
    log.info("Creating AISInputStreamAdapter");
    this.inputStream = inputStream;
  }

  public AISInputStreamAdapter(InputStream inputStream, String url, long delay, boolean autostart) throws UnhandledMessageException, IOException, URISyntaxException {
    log.info("Creating AISInputStreamAdapter");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {return;}

    this.inputStream = inputStream;
    if(autostart){
      post(new URL(url), delay);
    }
  }

  public void post(URL url) throws URISyntaxException, IOException, UnhandledMessageException {
    post(url, 0);
  }

  public void post(URL url, long delay) throws URISyntaxException, IOException, UnhandledMessageException{
    HttpClient http = getHttpClient();
    PostMethod post = new PostMethod(url.toString());

    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(this.inputStream));
    while(bufferedReader.ready()){
      String message = "";
      try{
        message = this.getMessage(bufferedReader);
      }catch(UnhandledMessageException e){
        log.error(e.getMessage());
      }

      if(!message.isEmpty()) {
        RequestEntity entity = new StringRequestEntity(message, CONTENT_TYPE, "utf-8");

        post.setRequestHeader("Content-Type", CONTENT_TYPE);
        post.setRequestEntity(entity);

        log.info("Posting message " + message + " to " + url.toString());
        http.executeMethod(post);
      }

      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {return;}
    }
  }

  HttpClient getHttpClient(){
    return new HttpClient();
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
