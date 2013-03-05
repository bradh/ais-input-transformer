/**
 * Copyright (c) Lockheed Martin Corporation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version. 
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
package org.codice.ddf.transform.ais;

import org.codice.ddf.transform.ais.message.*;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TestAISDecoder {

	@Test()
	public void testParseSingleLine() {
    Decoder decoder = new Decoder();

    String str = "!AIVDM,1,1,,B,177KQJ5000G?tO`K>RA1wUbN0TKH,0*5C";

    try{
      List<Message> messages = decoder.parseString(str);

      assertEquals(1, messages.size());
      Message1 message = (Message1) messages.get(0);
      assertEquals(1, message.getMessageType());
      assertEquals(477553000, message.getMmsi());
      assertEquals(-122.34583, message.getLon(), 0);
      assertEquals(51.0, message.getCog(), 0);

    }catch (Exception e){
      e.printStackTrace();
      fail(e.getMessage());
    }
	}

  @Test()
  public void testParseSingleLine3() {
    Decoder decoder = new Decoder();

    String str = "!AIVDM,1,1,,A,36:>9410008aN0nB>eubA4JO2000,0*1E,rORBCOMM000u,1361899938,1361899956";

    try{
      List<Message> messages = decoder.parseString(str);

      assertEquals(1, messages.size());
      Message1 message = (Message1) messages.get(0);
      assertEquals(3, message.getMessageType());
      assertEquals(413370640, message.getMmsi());
      assertEquals(120.90713833333334, message.getLon(), 0);
      assertEquals(262.8, message.getCog(), 0);

    }catch (Exception e){
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test()
  public void testSingleLine18() throws IOException, UnknownMessageException {
    Decoder decoder = new Decoder();
    String str = "!AIVDM,1,1,,B,B3=9=R000089kSV?44k5wwWUkP06,0*0E,rORBCOMM000u,1361899938,1361899956";

    List<Message> messages = decoder.parseString(str);
    assertEquals(1,messages.size());

    Message18 message = (Message18) messages.get(0);
    assertEquals(18, message.getMessageType());
    assertEquals(215109000, message.getMmsi());
    assertEquals(7.124385, message.getLon(), 0);
    assertEquals(316.7, message.getCog(), 0);
  }

  @Test()
  public void testParseMultiline5() {
    Decoder decoder = new Decoder();

    String str = "!AIVDM,2,1,2,A,5:LR1`000000iN3;KS58Tv0MD5aF22222222221?6p:5562:06T53p0T,0*68,rEXACTEARTH_ALL,1361887931\n" +
                 "!AIVDM,2,2,2,A,p0Dp13PH1`88880,2*19,rEXACTEARTH_ALL,1361887931,1361898118";

    try{
      List<Message> messages = decoder.parseString(str);
      assertEquals(1,messages.size());

      Message5 message = (Message5) messages.get(0);
      assertEquals(5, message.getMessageType());
      assertEquals("LW 268", message.getCallSign());
      assertEquals("PTO BS AS DNA F", message.getDestination());
      assertEquals(10, message.getEtaHour());

    }catch (Exception e){
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test()
  public void testUnknownMessage24(){
    Decoder decoder = new Decoder();
    String str = "!AIVDM,1,1,,B,H33qrT4NC9=B140?Cljij00H7220,0*46,rORBCOMM000u,1361899938,1361899956";
    try {
      decoder.parseString(str);
    } catch (IOException e) {
      fail(e.getMessage());
    } catch (UnknownMessageException e) {
      assertEquals("Message of type 24 is currently unsupported.", e.getMessage());
    }
  }

}
