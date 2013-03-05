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
package org.codice.ddf.transform.ais.message;

import java.util.List;

/**
 * User: kwplummer
 * Date: 3/4/13
 * Time: 3:31 PM
 */
public abstract class Message {
  private long messageType;
  private long repeatIndicator;
  private long mmsi;

  public static Message parseMessage(List<Byte> bitList) throws UnknownMessageException {
    byte [] bitVector = ByteListTobyteArray(bitList);
    int messageType = (int)bin2Dec(bitVector, 0);

    Message message = null;
    switch(messageType)
    {
      case 1:
      case 2:
      case 3:
        message = new Message1();
        break;
      case 4:

        break;
      case 5:
        message = new Message5();
        break;
      case 6:
        message = new Message6();
        break;
      case 8:
        message = new Message8();

        break;


      case 18:
        message = new Message18();

        break;
      case 19:
        message = new Message19();



        break;
      default:
        throw new UnknownMessageException("Message of type " + messageType + " is currently unsupported.");
    }
    message.parse(bitVector);
    return message;
  }

  abstract protected void parse(byte [] bitVector);

  /**
   * Returns the sign of the value passed in
   */
  static int sign(double v){
    return v < 0 ? -1 : 1;
  }

  static byte [] ByteListTobyteArray(List<Byte> bigBytes){
    byte [] bytes = new byte[bigBytes.size()];
    for(int ii = 0; ii < bytes.length; ++ii){
      bytes[ii] = bigBytes.get(ii).byteValue();
    }
    return bytes;
  }

  static double bin2Dec(byte [] bitVector, int offset)
  {
    double result = 0;

    for(int ii = offset; ii <= offset +5; ii++)
      result = result*2 + bitVector[ii];

    return result;
  }

  static boolean bin2bool(byte [] bitVector, int offset)
  {
    return bitVector[offset]>0?true:false;
  }

  static long bin2dec(byte [] bitVector, int startBit, int endBit){
    return bin2dec( bitVector, startBit, endBit, false);
  }

  static long bin2dec(byte [] bitVector, int startBit, int endBit, boolean isSigned)
  {
    long result = 0;

    if(isSigned)
    {
      boolean firstTrueFlag = false;
      byte [] binTC = new byte[endBit-startBit];
//      (0..(endBit-startBit)).each{binTC << 0}
      if(bitVector[startBit] > 0) //negative
      {
        int TCind = binTC.length-1;

        for(int p = endBit; p > startBit; p--){
          if(firstTrueFlag)
          {
            binTC[TCind] = bitVector[p] > 0 ? (byte)0:(byte)1;
          }
          else
          {
            binTC[TCind]=bitVector[p];
            if(bitVector[p] >= 0) firstTrueFlag=true;
          }
          --TCind;
        }
        // result = bin2dec(binTC, 0, binTC.size()-1)
        for(int ii = 0; ii <= binTC.length-1; ii++){
          result = result*2 + binTC[ii];
        }
        result = -result;
      }
      else
      {
        for(int ii = startBit + 1; ii <= endBit; ii++){
          result = result*2 + bitVector[ii];
        }
      }
    }
    else
    {
      for(int ii = startBit; ii <= endBit; ii++){
        result = result*2 + bitVector[ii];
      }
    }

    return result;
  }


  static String stripAisAsciiGarbage(Object inputValue)
  {
    // we will use quotes so it will also work with StringBuffer inputs as well
    return inputValue.toString().replaceAll("@.*", "").trim();
  }


  static String bin2SixBitAISAscii(byte [] bitVector, int startBit, int endBit)
  {
    String result = "";
    int length = (endBit-startBit)/6;
    int startBitIdx = startBit;
    for(int ii = 0 ; ii < length; ++ii){
      int v = (int)bin2dec(bitVector, startBitIdx, startBitIdx+5);
      switch(v)
      {
        case 0:
          result += "@";
          break;
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
          result += String.valueOf((char)(((byte)'A') + v - 1));
          break;
        case 48:
        case 49:
        case 50:
        case 51:
        case 52:
        case 53:
        case 54:
        case 55:
        case 56:
          result += String.valueOf((char)(((byte)'0') + v - 48));
          break;
        case 27:
          result += "[";
          break;
        case 28:    //"\"
          result += "\\";
          break;
        case 29:
          result += "]";
          break;
        case 30:
          result += "\\^";
          break;
        case 31:    //
          result += "\\_";
          break;
        case 32:
          result += " ";
          break;
        case 33:    //!
          result += "!";
          break;
        case 34:
          result += "\"";
          break;
        case 35:
          result += "\\#";
          break;
        case 36:    //$
          result += "$";
          break;
        case 37:
          result += "%";
          break;
        case 38:
          result += "&";
          break;
        case 39 :
          result += "\\";
          break;
        case 40:
          result += "(";
          break;
        case 41:    //)
          result += ")";
          break;
        case 42:
          result += "\\*";
          break;
        case 43:
          result += "\\+";
          break;
        case 44:
          result += ",";
          break;
        case 45:
          result += "-";
          break;
        case 46:
          result += ".";
          break;
        case 47:
          result += "/";
          break;
      }
      startBitIdx += 6;
    }


    return result;
  }



  public long getMessageType() {
    return messageType;
  }

  public void setMessageType(long messageType) {
    this.messageType = messageType;
  }

  public long getRepeatIndicator() {
    return repeatIndicator;
  }

  public void setRepeatIndicator(long repeatIndicator) {
    this.repeatIndicator = repeatIndicator;
  }

  public long getMmsi() {
    return mmsi;
  }

  public void setMmsi(long mmsi) {
    this.mmsi = mmsi;
  }
}
