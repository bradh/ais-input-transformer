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

/**
 * User: kwplummer
 * Date: 3/4/13
 * Time: 4:07 PM
 */
public class Message5 extends Message {

  private long aisVersion;
  private long imoNumber;
  private String callSign;
  private String vesselName;
  private long vesselTypeInt;
  private long length;
  private long width;
  private long typeOfEpfd;
  private long etaMonth;
  private long etaDay;
  private long etaHour;
  private long etaMinute;
  private long draught;
  private String destination;
  private boolean dte;

  @Override
  protected void parse(byte[] bitVector) {

    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));

    this.aisVersion = bin2dec(bitVector, 38, 39);
    this.imoNumber = bin2dec(bitVector, 40, 69);
    this.callSign = stripAisAsciiGarbage(bin2SixBitAISAscii(bitVector, 70, 111));
    this.vesselName = stripAisAsciiGarbage(bin2SixBitAISAscii(bitVector,112,231));
    this.vesselTypeInt = bin2dec(bitVector, 232, 239);
    this.length = bin2dec(bitVector, 240, 248) + bin2dec(bitVector, 249, 257);
    this.width = bin2dec(bitVector, 258,263) + bin2dec(bitVector, 264, 269);
    this.typeOfEpfd = bin2dec(bitVector, 270,273);
    this.etaMonth = bin2dec(bitVector, 274,277);
    this.etaDay = bin2dec(bitVector, 278,282);
    this.etaHour = bin2dec(bitVector, 283,287);
    this.etaMinute = bin2dec(bitVector, 288,293);
    this.draught = bin2dec(bitVector,294,301);
    this.destination = stripAisAsciiGarbage(bin2SixBitAISAscii(bitVector,302,421));
    this.dte = bin2bool(bitVector,422);
  }

  public long getAisVersion() {
    return aisVersion;
  }

  public void setAisVersion(long aisVersion) {
    this.aisVersion = aisVersion;
  }

  public long getImoNumber() {
    return imoNumber;
  }

  public void setImoNumber(long imoNumber) {
    this.imoNumber = imoNumber;
  }

  public String getCallSign() {
    return callSign;
  }

  public void setCallSign(String callSign) {
    this.callSign = callSign;
  }

  public String getVesselName() {
    return vesselName;
  }

  public void setVesselName(String vesselName) {
    this.vesselName = vesselName;
  }

  public long getVesselTypeInt() {
    return vesselTypeInt;
  }

  public void setVesselTypeInt(long vesselTypeInt) {
    this.vesselTypeInt = vesselTypeInt;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public long getWidth() {
    return width;
  }

  public void setWidth(long width) {
    this.width = width;
  }

  public long getTypeOfEpfd() {
    return typeOfEpfd;
  }

  public void setTypeOfEpfd(long typeOfEpfd) {
    this.typeOfEpfd = typeOfEpfd;
  }

  public long getEtaMonth() {
    return etaMonth;
  }

  public void setEtaMonth(long etaMonth) {
    this.etaMonth = etaMonth;
  }

  public long getEtaDay() {
    return etaDay;
  }

  public void setEtaDay(long etaDay) {
    this.etaDay = etaDay;
  }

  public long getEtaHour() {
    return etaHour;
  }

  public void setEtaHour(long etaHour) {
    this.etaHour = etaHour;
  }

  public long getEtaMinute() {
    return etaMinute;
  }

  public void setEtaMinute(long etaMinute) {
    this.etaMinute = etaMinute;
  }

  public long getDraught() {
    return draught;
  }

  public void setDraught(long draught) {
    this.draught = draught;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public boolean isDte() {
    return dte;
  }

  public void setDte(boolean dte) {
    this.dte = dte;
  }
}
