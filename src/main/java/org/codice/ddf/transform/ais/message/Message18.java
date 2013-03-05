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


public class Message18 extends Message {
  private double sog;
  private double lon;
  private double lat;
  private double cog;
  private long trueHeading;
  private long timestamp;
  private boolean csUnit;
  private boolean displayFlag;
  private boolean dscFlag;
  private boolean bandFlag;
  private boolean message22Flag;
  private boolean assigned;
  private boolean raimFlag;
  private long radioStatus;

  @Override
  protected void parse(byte[] bitVector) {
    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));
    
    this.sog = bin2dec(bitVector,46, 55)/10.0;
    this.lon = bin2dec(bitVector,57, 84, true)/600000.0;
    this.lat = bin2dec(bitVector,85, 111, true)/600000.0;
    this.cog = bin2dec(bitVector,112, 123)/10.0;
    this.trueHeading = bin2dec(bitVector,124,132);
    this.timestamp = bin2dec(bitVector, 133,138);
    this.csUnit = bin2bool(bitVector, 141);
    this.displayFlag = bin2bool(bitVector, 142);
    this.dscFlag = bin2bool(bitVector,143);
    this.bandFlag = bin2bool(bitVector, 144);
    this.message22Flag = bin2bool(bitVector, 145);
    this.assigned = bin2bool(bitVector,146);
    this.raimFlag = bin2bool(bitVector, 147);
    this.radioStatus = bin2dec(bitVector, 148, 167);
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

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isCsUnit() {
    return csUnit;
  }

  public void setCsUnit(boolean csUnit) {
    this.csUnit = csUnit;
  }

  public boolean isDisplayFlag() {
    return displayFlag;
  }

  public void setDisplayFlag(boolean displayFlag) {
    this.displayFlag = displayFlag;
  }

  public boolean isDscFlag() {
    return dscFlag;
  }

  public void setDscFlag(boolean dscFlag) {
    this.dscFlag = dscFlag;
  }

  public boolean isBandFlag() {
    return bandFlag;
  }

  public void setBandFlag(boolean bandFlag) {
    this.bandFlag = bandFlag;
  }

  public boolean isMessage22Flag() {
    return message22Flag;
  }

  public void setMessage22Flag(boolean message22Flag) {
    this.message22Flag = message22Flag;
  }

  public boolean isAssigned() {
    return assigned;
  }

  public void setAssigned(boolean assigned) {
    this.assigned = assigned;
  }

  public boolean isRaimFlag() {
    return raimFlag;
  }

  public void setRaimFlag(boolean raimFlag) {
    this.raimFlag = raimFlag;
  }

  public long getRadioStatus() {
    return radioStatus;
  }

  public void setRadioStatus(long radioStatus) {
    this.radioStatus = radioStatus;
  }
}
