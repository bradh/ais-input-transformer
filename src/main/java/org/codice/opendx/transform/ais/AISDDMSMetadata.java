package org.codice.opendx.transform.ais;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kwplummer
 * Date: 3/28/13
 * Time: 1:00 PM
 */
public class AISDDMSMetadata {
  public static final String MMSI = "MMSI";
  public static final String SPEED_OVER_GROUND = "SPEED_OVER_GROUND";
  public static final String TRUE_HEADING = "TRUE_HEADING";
  public static final String LAT = "LAT";
  public static final String LON = "LON";
  public static final String POINTS = "POINTS";
  public static final String ID = "ID";
  public static final String ID_ = "ID_";

  private String metadata;
  private List<String> points;

  private final String AIS_METADATA_MESSAGE =
          "<ddms:Resource xmlns:ddms=\"http://metadata.dod.mil/mdr/ns/DDMS/2.0/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:ICISM=\"urn:us:gov:ic:ism:v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metadata.dod.mil/mdr/ns/DDMS/2.0/ DDMS-v2_0.xsd\" xmlns:gml=\"http://www.opengis.net/gml\">\n" +
          "  <ddms:identifier ddms:qualifier=\"http://www.nmea.org/\" ddms:value=\"AISVDO\"/>\n" +
          "  <ddms:title  ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\" > MMSI </ddms:title>\n" +
          "  <ddms:publisher ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\">\n" +
          "   <ddms:Organization>\n" +
          "    <ddms:name>RadiantBlue</ddms:name>\n" +
          "   </ddms:Organization>\n" +
          "  </ddms:publisher> \n" +
          "  <ddms:subjectCoverage>\n" +
          "   <ddms:Subject>\n" +
          "    <ddms:category ddms:code=\"SPEED_OVER_GROUND\" ddms:qualifier=\"http://www.nmea.org/\" ddms:label=\"SpeedOverGround\"/>\n" +
          "    <ddms:category ddms:code=\"TRUE_HEADING\" ddms:qualifier=\"http://www.nmea.org/\" ddms:label=\"TrueHeading\"/>\n" +
          "   </ddms:Subject>\n" +
          "  </ddms:subjectCoverage>\n" +
          "  <ddms:geospatialCoverage>\n" +
          "\t\t<ddms:GeospatialExtent>\n" +
          "\t\t\t<ddms:boundingGeometry>\n" +
          "\t\t\t\t<!-- Multiple points in here if needed -->\n" +
          "POINTS" +
          "\t\t\t</ddms:boundingGeometry>\n" +
          "\t\t</ddms:GeospatialExtent>\n" +
          "\t</ddms:geospatialCoverage>\n" +
          "  <ddms:security ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\" />\n" +
          " </ddms:Resource>\n";

  private final String AIS_METADATA_POINT =
          "\t\t\t\t<gml:Point srsName=\"EPSG:4326\" gml:id=\"ID\">\n" +
          "\t\t\t\t\t<gml:pos>LAT LON</gml:pos>\n" +
          "\t\t\t\t</gml:Point>\n";


  public AISDDMSMetadata(String mmsi, Double speedOverGround, Double trueHeading){
    this.metadata = AIS_METADATA_MESSAGE.replace(MMSI, mmsi)
            .replace(SPEED_OVER_GROUND, String.valueOf(speedOverGround))
            .replace(TRUE_HEADING, String.valueOf(trueHeading));
    this.points = new ArrayList<String>();
  }

  public void addPoint(Double lat, Double lon){
    this.points.add(
            AIS_METADATA_POINT
                    .replace(LAT, String.valueOf(lat))
                    .replace(LON, String.valueOf(lon))
                    .replace(ID, ID_ + String.valueOf(this.points.size() + 1))
    );
  }

  public String toString(){
    String pointsString = "";
    for(String point : this.points){
      pointsString += point;
    }

    return this.metadata.replace(POINTS, pointsString);
  }

}
