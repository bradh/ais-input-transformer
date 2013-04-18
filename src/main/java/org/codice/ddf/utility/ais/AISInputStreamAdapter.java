/**
 * Reads AIS Sentences from an input stream and posts them to and http endpoint.  It will read message fragments and
 * post only when the message is complete.
 */

package org.codice.ddf.utility.ais;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class AISInputStreamAdapter {

  private static final Logger log = Logger.getLogger(AISInputStreamAdapter.class);

  private static final String CONTENT_TYPE = "application/ais-nmea";
  private InputStream inputStream;
  private String url;

  private HttpClient httpClient;
  private boolean open = false;
  private boolean async = false;

  public AISInputStreamAdapter(InputStream inputStream) {
    log.info("Creating AISInputStreamAdapter");
    this.inputStream = inputStream;
  }

  public AISInputStreamAdapter(InputStream inputStream, String url) throws UnhandledMessageException, IOException, URISyntaxException {
    log.info("Creating AISInputStreamAdapter");

    this.inputStream = inputStream;
    this.url = url;
  }

  public void init() throws IOException, UnhandledMessageException, URISyntaxException {
    log.info("Starting AISInputStreamAdapter");
    this.open = true;
    this.async = true;
    post(new URL(url));
  }

  public void destroy() throws IOException {
    log.info("Stopping AISInputStreamAdapter");
    this.open = false;
    if(this.inputStream != null)
      this.inputStream.close();

  }

  public void post(final URL url) throws URISyntaxException, IOException, UnhandledMessageException {
    Thread thread = new Thread(){
      @Override
      public void run(){
        HttpClient http = getHttpClient();
        PostMethod post = new PostMethod(url.toString());

        while(open){
          try{
            String message = "";
            try{
              message = getMessage(inputStream);
            }catch(UnhandledMessageException e){
              log.error(e.getMessage());
            }

            if(!message.isEmpty()) {
              RequestEntity entity = new StringRequestEntity(message, CONTENT_TYPE, null);

              post.setRequestHeader("Content-Type", CONTENT_TYPE);
              post.setRequestEntity(entity);
              post.setRequestHeader("Content-Length", String.valueOf(message.length()));
              log.info("Posting message " + message + " to " + url.toString());

              int response = http.executeMethod(post);
              log.info("Received response " + response + " from POST");
            }
          }catch (Exception e){
            if(open)
              log.error(e);
          }
        }
      }
    };
    thread.start();
    if(!async){
      try {
        thread.join();
      } catch (InterruptedException e) {}
    }
  }

  HttpClient getHttpClient(){
    if(this.httpClient == null)
      this.httpClient = new HttpClient();
    return this.httpClient;
  }

  private String [] getTokens(InputStream reader) throws IOException {
    try{
      String line = "";//reader.readLine();
      int integer = reader.read();
      while(integer != -1 && (char)integer != '\n'){
        line += (char)integer;
        integer = reader.read();
      }
      log.info("Read Line: " + line);
      if(line == null)
        return null;
      String [] tokens = line.split(",");
      return tokens;
    }catch (IOException e) {
      if(this.open)
        throw e;
    }
    return null;
  }

  private String getMessage(InputStream reader) throws IOException, UnhandledMessageException {
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
