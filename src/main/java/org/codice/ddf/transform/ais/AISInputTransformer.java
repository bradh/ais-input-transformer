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
import ddf.catalog.federation.FederationException;
import ddf.catalog.operation.*;
import ddf.catalog.source.SourceUnavailableException;
import ddf.catalog.source.UnsupportedQueryException;
import ddf.catalog.transform.CatalogTransformerException;

import org.apache.log4j.Logger;
import org.codice.common.ais.Decoder;
import org.codice.common.ais.message.Message;
import org.codice.common.ais.message.Message5;
import org.codice.common.ais.message.UnknownMessageException;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


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
    MetacardImpl metacard = null;

    for(Message message : messages){
      metacard = getMetacard(message);
      if(metacard == null){
        metacard = new MetacardImpl();
        metacard.setId(String.valueOf(message.getMmsi()));
        if(message instanceof Message5){
          metacard.setTitle(((Message5) message).getCallSign());
        }else{
          metacard.setTitle(String.valueOf(message.getMmsi()));
        }
        metacard.setContentTypeName("application/ais-nmea");
        metacard.setModifiedDate(new Date());
        if(message.hasLocationData())
          metacard.setLocation(WKTWriter.toPoint(new Coordinate(message.getLon(), message.getLat())));

        metacard.setMetadata(getResourceForMessage(message));
      }
    }
    log.info("Metacard " + metacard.getTitle() + " created");
    return metacard;
  }

  private MetacardImpl getMetacard(Message message){

    FilterFactory filterFactory = new FilterFactoryImpl() ;
    Filter filter = filterFactory.like(filterFactory.property(Metacard.ID), String.valueOf(message.getMmsi()));
    Query query = new QueryImpl(filter);

    Collection<String> ids = new ArrayList<String>();
    ids.add(String.valueOf(message.getMmsi()));
    QueryRequest request = new QueryRequestImpl(query);
    try {
      QueryResponse response = this.catalog.query(request);
      if(!response.getResults().isEmpty()){
        
        return (MetacardImpl) response.getResults().get(0).getMetacard();
      }
    } catch (UnsupportedQueryException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (SourceUnavailableException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (FederationException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally {
      return null;
    }
  }

  private String getResourceForMessage(Message message) {
    AISDDMSMetadata metadata = new AISDDMSMetadata(String.valueOf(message.getMmsi()), null, null);
    if(message.hasLocationData())
      metadata.addPoint(message.getLat(), message.getLon());
    return metadata.toString();
  }

  public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  public void setCatalog(CatalogFramework catalog) {
    this.catalog = catalog;
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