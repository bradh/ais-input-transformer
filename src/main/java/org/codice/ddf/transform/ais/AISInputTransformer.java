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

package org.codice.ddf.transform.ais;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.WKTWriter;
import ddf.catalog.CatalogFramework;
import ddf.catalog.data.Metacard;
import ddf.catalog.data.MetacardImpl;
import ddf.catalog.transform.CatalogTransformerException;
import org.apache.log4j.Logger;
import org.codice.common.ais.Decoder;
import org.codice.common.ais.message.Message;
import org.codice.common.ais.message.Message5;
import org.codice.common.ais.message.UnknownMessageException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class AISInputTransformer implements ddf.catalog.transform.InputTransformer {

  private static final Logger log = Logger.getLogger(AISInputTransformer.class);

  public static final SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  static {
    ISO_8601_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  private CatalogFramework catalog;

  @Override
  public Metacard transform(InputStream inputStream) throws IOException, CatalogTransformerException {
    return transform(inputStream, null);
  }


  @Override
  public Metacard transform(InputStream inputStream, String id) throws IOException, CatalogTransformerException {
    log.info("Received AIS message beginning transformation");

    if (inputStream == null) {
      throw new CatalogTransformerException("Cannot transform null input.");
    }

    Decoder decoder = new Decoder();

    List<Message> messages;
    try {
      messages = decoder.parseInputStream(inputStream);
    } catch (UnknownMessageException e) {
      log.error(e.getMessage());
      throw new CatalogTransformerException(e);
    }
    if(messages.isEmpty())
      throw new CatalogTransformerException("No Messages found in stream.");
    log.info("Decoded " + messages.size() + " message(s)");

    Message message = messages.get(0);
    MetacardImpl metacard = new MetacardImpl();
    metacard.setId(String.valueOf(message.getMmsi()));
    metacard.setAttribute("MMSI", String.valueOf(message.getMmsi()));

    if(message instanceof Message5){
      metacard.setTitle(((Message5) message).getVesselName()  + "(" + ((Message5) message).getCallSign() + ")"  );
    }else{
      metacard.setTitle( String.valueOf(message.getMmsi()) );
    }

    metacard.setContentTypeName("application/ais-nmea");
    metacard.setModifiedDate(new Date());
    if(message.hasLocationData())
      metacard.setLocation(WKTWriter.toPoint(new Coordinate(message.getLat(), message.getLon())));

    metacard.setMetadata(getResourceForMessage(message));
    log.info("Metacard " + metacard.getTitle() + "(" + metacard.getId() + ")" + " created");
    return metacard;

  }

  private String getResourceForMessage(Message message) {
    AISDDMSMetadata metadata = new AISDDMSMetadata(String.valueOf(message.getMmsi()), null, null);
    if(message.hasLocationData())
      metadata.addPoint(message.getLat(), message.getLon());
    return metadata.toString();
  }

  public void setCatalog(CatalogFramework catalog) {
    this.catalog = catalog;
  }
}