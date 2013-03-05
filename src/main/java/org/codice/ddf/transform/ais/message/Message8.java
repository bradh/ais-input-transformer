package org.codice.ddf.transform.ais.message;

import java.util.Arrays;

/**
 * User: kwplummer
 * Date: 3/4/13
 * Time: 4:33 PM
 */
public class Message8 extends Message {


  private long designatedAreaCode;
  private long functionalId;
  private byte[] data;

  @Override
  protected void parse(byte[] bitVector) {

    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));

    
    this.designatedAreaCode = bin2dec(bitVector, 40, 49);
    this.functionalId = bin2dec(bitVector, 50, 55);
    this.data = Arrays.copyOfRange(bitVector, 56, 920);
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
