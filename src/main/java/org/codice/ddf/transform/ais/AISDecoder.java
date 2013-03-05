package org.codice.ddf.transform.ais;

import org.codice.ddf.transform.ais.message.Message;
import org.codice.ddf.transform.ais.message.UnknownMessageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


import java.util.regex.Pattern;


class AISDecoder
{

  /**
   * processPayloadType will use the bitVector of the payload to extract
   * out the type's values and puts them into a HashMap of name value pairs
   * The value should be in it's native type.  If its and integer type then it will be
   * a Java Integer value and if it's a string it will be a Java String type
   *
   * @param bitList
   * @return
   */
  public Message processPayloadType( List<Byte> bitList) throws UnknownMessageException {
    return Message.parseMessage(bitList);
  }



  /**
   * We use the eachLine call in groovy and should also work for InputStream types
   * So in could be a string with end-of-line characters or could be an InputStream
   */
  List<Message> parse(InputStream inputStream) throws IOException
  {
//    def nmeaMessage = new NmeaMessage()
    String [] pghpMessage = null;
    String fullPayload = "";

    List<Message> messages = new ArrayList<Message>();
    List aisMessages = new ArrayList<String[]>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line = null;
    while((line = reader.readLine()) != null){

    String [] fields = line.substring(1).split(",");
    if(fields[0].equals("PGHP"))
      {
        //noop
      }
      else if(fields[0].equals( "AIVDM"))
      {
//        def fieldList = []
//        fields.each{field->fieldList << field}
        aisMessages.add(fields);
        if(fields[1].equals(fields[2]))
        {
//          def pghpMessageList
//          if(pghpMessage)
//          {
//            pghpMessageList = []
//            pghpMessage.nmeaFields.each{field-> pghpMessageList << field}
//            pghpMessage = null
//          }
          try{
            messages.add(processMessage(pghpMessage, aisMessages));
          }catch(UnknownMessageException e){
            //log error
          }
          aisMessages = new ArrayList<String[]>();
        }
      }
      else
      {
        pghpMessage = null;
      }
    }
    return messages;
  }

  /**
   * Ais messages seems to have 2 ways of doing timestamping.  First method is
   * the PGHP messsage or the AIVDM message with epoch at the end of the AIVDM
   * message.
   *
   * processMessage method is called once all fragments are available and if prsent the pghp is
   * also passed in.
   *
   * @param pghp
   * @param aisMessages
   * @return
   */
  Message processMessage(String [] pghp, List<String[]> aisMessages) throws UnknownMessageException {
    String fullPayload = "";
//    def dateTime;
//    if(pghp)
//    {
//      def year    = pghp[2]
//      def month   = pghp[3]
//      def day     = pghp[4]
//      def hours   = pghp[5]
//      def minutes = pghp[6]
//      def seconds = pghp[7]
//      dateTime = "${year}-${month}-${day}T${hours}:${minutes}:${seconds}Z".toDateTime().toDateTime(DateTimeZone.UTC)
//    }
//    else
//    {
      // test for unix EPOCH
      //
      // and we will take the stamp from the last message and the last field
      String lastField = "*";
      for(String [] message : aisMessages){
        lastField = message[message.length-1];
      }

      if(!Pattern.compile( "/\\*/" ).matcher( lastField ).find())
      {
        try
        {
          Date dateTime = new Date(Long.parseLong(lastField)*1000);

        }
        catch(Exception e)
        {

          // log error
        }

      }
//    }
//    if(dateTime)
//    {
//      // println dateTime
//      // println new Date(dateTime.millis)
//    }
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



    return processPayloadType(bitVector);

  }


}




