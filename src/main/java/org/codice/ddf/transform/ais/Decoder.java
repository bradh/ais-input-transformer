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

import org.codice.ddf.transform.ais.message.Message;
import org.codice.ddf.transform.ais.message.UnknownMessageException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


class Decoder
{

  /**
   * Takes a string of single or multiple line NMEA/AIS/AIVDM sentences
   * @param sentence
   * @return array of messages parsed from the string
   * @throws IOException
   * @throws UnknownMessageException
   */
  List<Message> parseString(String sentence) throws IOException, UnknownMessageException {
    return parseInputStream(new ByteArrayInputStream(sentence.getBytes()));
  }

  /**
   * Takes an input stream of NMEA/AIS/AIVDM sentences
   * @param inputStream
   * @return array of messages parsed from the stream
   * @throws IOException
   * @throws UnknownMessageException
   */
  List<Message> parseInputStream(InputStream inputStream) throws IOException, UnknownMessageException {
    List<Message> messages = new ArrayList<Message>();
    List aisMessages = new ArrayList<String[]>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    while((line = reader.readLine()) != null){

    String [] fields = line.substring(1).split(",");

    if(fields[0].equals( "AIVDM"))
    {
        aisMessages.add(fields);
        if(fields[1].equals(fields[2]))
        {
          messages.add(processMessage(aisMessages));

          aisMessages = new ArrayList<String[]>();
        }
      }
    }
    return messages;
  }


  private Message processMessage(List<String[]> aisMessages) throws UnknownMessageException {
    String fullPayload = "";

    for(String [] message : aisMessages){
      fullPayload+=message[5];
    }
    ArrayList<Byte> bitVector= new ArrayList<Byte>();
    // create your bit vector of 0's and 1's of byte type

    for(byte bite : fullPayload.getBytes()){
      byte byteChar = (byte)(bite-48);
      byteChar = byteChar > 40 ? (byte)(byteChar-8) : (byte)byteChar;
      bitVector.add((byte)((byteChar>>>5)&1));
      bitVector.add((byte)((byteChar>>>4)&1));
      bitVector.add((byte)((byteChar>>>3)&1));
      bitVector.add((byte)((byteChar>>>2)&1));
      bitVector.add((byte)((byteChar>>>1)&1));
      bitVector.add((byte)(byteChar&1));
    }
    return Message.parseMessage(bitVector);
  }


}




