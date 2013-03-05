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

import java.util.Arrays;

/**
 * User: kwplummer
 * Date: 3/4/13
 * Time: 4:25 PM
 */
public class Message6 extends Message {

  private long sequenceNumber;
  private long destinationMmsi;
  private boolean retransmitFlag;
  private long designatedAreaCode;
  private long functionalId;
  private byte[] data;

  @Override
  protected void parse(byte[] bitVector) {
    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));

    this.sequenceNumber = bin2dec(bitVector, 38, 39);
    this.destinationMmsi = bin2dec(bitVector, 40, 69);
    this.retransmitFlag = bin2bool(bitVector, 70);
    this.designatedAreaCode = bin2dec(bitVector, 72, 81);
    this.functionalId = bin2dec(bitVector, 82, 87);
    this.data = Arrays.copyOfRange(bitVector, 88, 920);
  }


  public long getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public long getDestinationMmsi() {
    return destinationMmsi;
  }

  public void setDestinationMmsi(long destinationMmsi) {
    this.destinationMmsi = destinationMmsi;
  }

  public boolean isRetransmitFlag() {
    return retransmitFlag;
  }

  public void setRetransmitFlag(boolean retransmitFlag) {
    this.retransmitFlag = retransmitFlag;
  }

  public long getDesignatedAreaCode() {
    return designatedAreaCode;
  }

  public void setDesignatedAreaCode(long designatedAreaCode) {
    this.designatedAreaCode = designatedAreaCode;
  }

  public long getFunctionalId() {
    return functionalId;
  }

  public void setFunctionalId(long functionalId) {
    this.functionalId = functionalId;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
