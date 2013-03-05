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

import org.codice.ddf.transform.ais.AISDecoder;
import org.codice.ddf.transform.ais.message.Message;
import org.codice.ddf.transform.ais.message.Message1;
import org.codice.ddf.transform.ais.message.Message5;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TestAISDecoder {

	@Test()
	public void testParseSingleLine() {
    AISDecoder decoder = new AISDecoder();

    String str = "!AIVDM,1,1,,B,177KQJ5000G?tO`K>RA1wUbN0TKH,0*5C";

    // convert String into InputStream
    InputStream is = new ByteArrayInputStream(str.getBytes());
    try{
      List<Message> messages = decoder.parse(is);

      assertEquals(1, messages.size());
      Message1 message = (Message1) messages.get(0);
      assertEquals(1, message.getMessageType());

    }catch (Exception e){
      e.printStackTrace();
      fail(e.getMessage());
    }
	}

  @Test()
  public void testParseMultiline() {
    AISDecoder decoder = new AISDecoder();

    String str = "!AIVDM,2,1,2,A,5:LR1`000000iN3;KS58Tv0MD5aF22222222221?6p:5562:06T53p0T,0*68,rEXACTEARTH_ALL,1361887931\n" +
                 "!AIVDM,2,2,2,A,p0Dp13PH1`88880,2*19,rEXACTEARTH_ALL,1361887931,1361898118";

    // convert String into InputStream
    InputStream is = new ByteArrayInputStream(str.getBytes());
    try{
      List<Message> messages = decoder.parse(is);
      assertEquals(1,messages.size());

      Message5 message = (Message5) messages.get(0);
      assertEquals(5, message.getMessageType());
    }catch (Exception e){
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
}
