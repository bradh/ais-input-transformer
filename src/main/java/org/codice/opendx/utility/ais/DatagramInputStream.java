
/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/**
  *****************************************************************
  ***                ***
  ***  Name :  DatagramInputStream                                 ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       This class extends InputStream, providing its API   ***
  ***   for calls to a UDPSocket.                               ***
  ***                ***
  *****************************************************************
*/
package org.codice.opendx.utility.ais;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramInputStream extends InputStream {
  private static final Logger log = Logger.getLogger(DatagramInputStream.class);


  private static final int PACKET_BUFFER_SIZE = 5000;

  DatagramSocket dsock = null;
  DatagramPacket dpack = null;

  byte[] ddata = new byte[PACKET_BUFFER_SIZE];
  int packSize = 0;
  int packIdx = 0;

  int value;

  /********************** constructors ********************/
/*
  *****************************************************************
  ***                ***
  ***  Name :  DatagramInputStream                                 ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       Default constructor.                                ***
  ***                ***
  *****************************************************************
*/
  public DatagramInputStream() {}

  /*
  *****************************************************************
  ***                ***
  ***  Name :  DatagramInputStream                                 ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       Constructor.  Requires the port of the  ***
  ***   UDP socket to read from.                                ***
  ***                ***
  *****************************************************************
*/
  public DatagramInputStream(int port)
          throws UnknownHostException, SocketException {

    open(null, port);
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  DatagramInputStream                                 ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Constructor.  Requires the address and port of the  ***
    ***   UDP socket to read from.                                ***
    ***                ***
    *****************************************************************
  */
  public DatagramInputStream(String address, int port)
          throws UnknownHostException, SocketException {

    open(address, port);
  }

  /************ opening and closing the stream ************/
/*
  *****************************************************************
  ***                ***
  ***  Name :  open                                             ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       The user may use this method to set the address and ***
  ***   port of the UDP socket to read from.                    ***
  ***                ***
  *****************************************************************
*/
  public void open(String address, int port)
          throws UnknownHostException, SocketException {
    if(address == null){
      dsock = new DatagramSocket(port);
    }else{
      dsock = new DatagramSocket(port, InetAddress.getByName(address));
    }
    log.info("Opened DatagramSocket port=" + port);
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  close                                       ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Close the UDP socket and DatagramInputStream.            ***
    ***                ***
    *****************************************************************
  */
  public void close() throws IOException {
    dsock.close();
    dsock = null;
    ddata = null;
    packSize = 0;
    packIdx = 0;
  }

  /****** reading, skipping and checking available data ******/
/*
  *****************************************************************
  ***                ***
  ***  Name :  available                                 ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       Determines how many more values may be read before  ***
  ***   next blocking read.                                     ***
  ***                ***
  *****************************************************************
*/
  public int available() throws IOException {
    log.info("checking available - " + (packSize - packIdx));
    return packSize - packIdx;
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  read()                                     ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Reads the next value available.  Returns the value  ***
    ***   as an integer from 0 to 255.                            ***
    ***                ***
    *****************************************************************
  */
  public int read() throws IOException {
    if (packIdx == packSize) {
      receive();
    }

    value = ddata[packIdx] & 0xff;
    packIdx++;
    return value;
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  read(byte[])                                 ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Reads the next buff.length values into the input    ***
    ***   byte array, buff.                                       ***
    ***                ***
    *****************************************************************
  */
  public int read(byte[] buff) throws IOException {
    return read(buff, 0, buff.length);
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  read(byte[], int, int)                         ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Reads the next len values into the input byte array,***
    ***   buff, starting at offset off.                           ***
    ***                ***
    *****************************************************************
  */
  public int read(byte[] buff, int off, int len) throws IOException {
    if (packIdx == packSize) {
      receive();
    }

    int lenRemaining = len;

    while(available() < lenRemaining) {
      System.arraycopy(ddata,
              packIdx,
              buff,
              off + (len - lenRemaining),
              available());
      lenRemaining -= available();
      receive();
    }

    System.arraycopy(ddata,
            packIdx,
            buff,
            off + (len - lenRemaining),
            lenRemaining);
    packIdx += lenRemaining;
    return len;
  }

  /*
    *****************************************************************
    ***                ***
    ***  Name :  skip                                       ***
    ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
    ***  For  :  E-Scan            ***
    ***  Date :  October, 2001          ***
    ***                ***
    ***  Copyright 2001 Creare Inc.        ***
    ***  All Rights Reserved          ***
    ***                ***
    ***  Description :            ***
    ***       Skips over the next len values.                     ***
    ***                ***
    *****************************************************************
  */
  public long skip(long len) throws IOException {
    if (packIdx == packSize) {
      receive();
    }

    long lenRemaining = len;

    while(available() < lenRemaining) {
      lenRemaining -= available();
      receive();
    }

    packIdx += (int) lenRemaining;
    return len;
  }

  /****************** receiving more data ******************/
/*
  *****************************************************************
  ***                ***
  ***  Name :  receive                                    ***
  ***  By   :  U. Bergstrom   (Creare Inc., Hanover, NH)  ***
  ***  For  :  E-Scan            ***
  ***  Date :  October, 2001          ***
  ***                ***
  ***  Copyright 2001 Creare Inc.        ***
  ***  All Rights Reserved          ***
  ***                ***
  ***  Description :            ***
  ***       A blocking read to receive more data from the UDP   ***
  ***   socket.                                                 ***
  ***                ***
  *****************************************************************
*/
  private void receive() throws IOException {
    dpack = new DatagramPacket(ddata, PACKET_BUFFER_SIZE);
    dsock.receive(dpack);
    packIdx = 0;
    packSize = dpack.getLength();
  }

  /********* marking and reseting are unsupported ********/
  public void mark(int readlimit) {}

  public void reset() throws IOException {
    throw new IOException("Marks are not supported by DatagramInputStream.");
  }

  public boolean markSupported() {
    return false;
  }

}
