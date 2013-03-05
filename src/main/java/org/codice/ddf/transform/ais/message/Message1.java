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


public class Message1 extends Message {

  private long navStatus;
  private double rot;
  private double sog;
  private boolean positionAccuracy;
  private double lon;
  private double lat;
  private double cog;
  private long trueHeading;
  private long timestamp;
  private long maneuverIndicator;
  private boolean raimFlag;
  private long radioStatus;

  @Override
  protected void parse(byte[] bitVector) {
    double rot = bin2dec(bitVector,42, 49);
    setMessageType(bin2dec(bitVector,0,5));
    setRepeatIndicator(bin2dec(bitVector,6,7));
    setMmsi((int)bin2dec(bitVector, 8, 37));

    setNavStatus(bin2dec(bitVector,38, 41));
    setRot((sign(rot)*(rot/4.733)*(rot/4.733)));
    setSog(bin2dec(bitVector,50, 59)/10.0);
    setPositionAccuracy(bin2bool(bitVector, 60));
    setLon(bin2dec(bitVector,61, 88, true)/600000.0);
    setLat(bin2dec(bitVector,89, 115, true)/600000.0);
    setCog(bin2dec(bitVector,116, 127)/10.0);
    setTrueHeading(bin2dec(bitVector,128,136));
    setTimestamp(bin2dec(bitVector,137,142));
    setManeuverIndicator(bin2dec(bitVector,143,144));
    setRaimFlag(bin2bool(bitVector, 148));
    setRadioStatus(bin2dec(bitVector,149,167));

  }

  public long getNavStatus() {
    return navStatus;
  }

  public void setNavStatus(long navStatus) {
    this.navStatus = navStatus;
  }

  public double getRot() {
    return rot;
  }

  public void setRot(double rot) {
    this.rot = rot;
  }

  public double getSog() {
    return sog;
  }

  public void setSog(double sog) {
    this.sog = sog;
  }

  public boolean getPositionAccuracy() {
    return positionAccuracy;
  }

  public void setPositionAccuracy(boolean positionAccuracy) {
    this.positionAccuracy = positionAccuracy;
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

  public long getManeuverIndicator() {
    return maneuverIndicator;
  }

  public void setManeuverIndicator(long maneuverIndicator) {
    this.maneuverIndicator = maneuverIndicator;
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
