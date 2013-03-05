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
