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

package org.codice.ddf.transform;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.WKTWriter;
import ddf.catalog.data.Metacard;
import ddf.catalog.data.MetacardImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.InputTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class AISInputTransformer implements InputTransformer  {

  private static final Logger log = Logger.getLogger(AISInputTransformer.class);

  public static final SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  static {
    ISO_8601_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public Metacard transform(InputStream inputStream) throws IOException, CatalogTransformerException {
    return transform(inputStream, null);
  }

  /**
   *
   * @param inputStream
   * @param id
   * @return
   * @throws IOException
   * @throws CatalogTransformerException
   */
  @Override
  public Metacard transform(InputStream inputStream, String id) throws IOException, CatalogTransformerException {
    if (inputStream == null) {
      throw new CatalogTransformerException("Cannot transform null input.");
    }

    String nmea = IOUtils.toString(inputStream);
    log.info("Parsing NMEA string \"" + nmea + "\"");

    MetacardImpl metacard = new MetacardImpl();
    String [] tokens = nmea.split(",");
    if(tokens.length != 24){
      throw new CatalogTransformerException("Missing Tokens in NMEA String " + nmea);
    }

    metacard.setId(tokens[0]);
    metacard.setTitle(tokens[10]);
    metacard.setContentTypeName("application/ais-nmea");
    metacard.setModifiedDate(new Date());
    metacard.setMetadata("<xml>"+tokens[10]+"</xml>");
    metacard.setLocation(WKTWriter.toPoint(new Coordinate(Double.parseDouble(tokens[5]), Double.parseDouble(tokens[4]))));

    return metacard;
  }
}


/**
 mmsi: tokens[0] as Integer,
 //navStatus: tokens[1] as Integer,
 rateOfTurn: tokens[2] as Float,
 speedOverGround: tokens[3] as Float,
 posAccuracy: tokens[21] as Double,
 courseOverGround: tokens[6] as Double,
 trueHeading: tokens[7] as Double,
 IMO: tokens[9] as Integer,
 callsign: tokens[20],
 vesselName: tokens[10],
 //vesselType: tokens[11] as Integer,            //XXX Need to replace this with type logic
 length: tokens[12] as Double,
 width: tokens[13] as Double,
 eta: ( new Date() + 30 ),
 int navCode = tokens[1] as Integer
 def longitude = tokens[5] as Double
 def latitude = tokens[4] as Double
*/