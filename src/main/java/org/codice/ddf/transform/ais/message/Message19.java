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
 * Time: 4:37 PM
 */
public class Message19 extends Message {
  private double sog;
  private double lon;
  private double lat;
  private double cog;
  private long trueHeading;
  private String vesselName;
  private long vesselTypeInt;
  private long length;
  private long width;

  @Override
  protected void parse(byte[] bitVector) {
    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));

    this.sog = (bin2dec(bitVector, 46, 55))/10.0;
    this.lon = bin2dec(bitVector,    57, 84,true)/600000.0;
    this.lat = bin2dec(bitVector,    85, 111,true)/600000.0;
    this.cog = (bin2dec(bitVector, 112, 123))/10.0;
    this.trueHeading = bin2dec(bitVector,124,132);
    this.vesselName = bin2SixBitAISAscii(bitVector,143,262);
    this.vesselTypeInt = bin2dec(bitVector,263,270);
    this.length = bin2dec(bitVector, 271, 279) + bin2dec(bitVector, 280, 288);
    this.width = bin2dec(bitVector, 289,294) + bin2dec(bitVector, 295, 300);
  }


  public double getSog() {
    return sog;
  }

  public void setSog(double sog) {
    this.sog = sog;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getCog() {
    return cog;
  }

  public void setCog(double cog) {
    this.cog = cog;
  }

  public long getTrueHeading() {
    return trueHeading;
  }

  public void setTrueHeading(long trueHeading) {
    this.trueHeading = trueHeading;
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
}
