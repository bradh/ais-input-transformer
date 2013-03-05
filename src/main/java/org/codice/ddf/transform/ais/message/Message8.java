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
