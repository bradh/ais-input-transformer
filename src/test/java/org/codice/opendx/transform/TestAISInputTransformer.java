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
package org.codice.opendx.transform;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import ddf.catalog.data.Metacard;
import ddf.catalog.transform.CatalogTransformerException;
import org.codice.opendx.transform.ais.AISInputTransformer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import ddf.catalog.operation.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestAISInputTransformer {
	private static final String SAMPLE_ID = "701006240";
	public static final String DEFAULT_TITLE = "LW 268";
	public static final String DEFAULT_VERSION = null;
	public static final String DEFAULT_TYPE = "application/ais-nmea";

  public static AISInputTransformer createTransformer() throws ddf.catalog.source.UnsupportedQueryException, ddf.catalog.source.SourceUnavailableException, ddf.catalog.federation.FederationException {
    AISInputTransformer transformer = new AISInputTransformer();
    ddf.catalog.CatalogFramework catalog = mock(ddf.catalog.CatalogFramework.class);
    when(catalog.query(any(QueryRequest.class))).thenReturn(new QueryResponseImpl(null, "sourceId"));
    transformer.setCatalog(catalog);

    return transformer;
  }


	@Test(expected = CatalogTransformerException.class)
	public void testNullInput() throws IOException, CatalogTransformerException {
		new AISInputTransformer().transform(null);
	}

	@Test(expected = CatalogTransformerException.class)
	public void testBadInput() throws IOException, CatalogTransformerException {
		new AISInputTransformer().transform(new ByteArrayInputStream("donkey".getBytes()));
	}

	@Test(expected = CatalogTransformerException.class)
	public void testNoProperties() throws IOException, CatalogTransformerException {
		new AISInputTransformer().transform(new ByteArrayInputStream("{ \"type\": \"FeatureCollection\"}"
				.getBytes()));
  }

	@Test()
	public void testPointGeo() throws IOException, CatalogTransformerException, Exception {
    AISInputTransformer transformer = createTransformer();

		Metacard metacard = transformer.transform(new ByteArrayInputStream(sampleNMEAString3()
				.getBytes()));


		WKTReader reader = new WKTReader();

		Geometry geometry = reader.read(metacard.getLocation());

		assertEquals(geometry.getCoordinate().x + " doesn't equal " + "-19.046145", String.valueOf(geometry.getCoordinate().x), "-19.046145");

		assertEquals(geometry.getCoordinate().y + " doesn't equal " + "-18.227776666666667", String.valueOf(geometry.getCoordinate().y), "-18.227776666666667");

	}

  @Test
  public void testSetId() throws IOException, CatalogTransformerException, Exception {

    Metacard metacard = createTransformer().transform(new ByteArrayInputStream(sampleNMEAString()
            .getBytes()), SAMPLE_ID);

    assertEquals(SAMPLE_ID + " doesn't equal " + metacard.getId(), SAMPLE_ID, metacard.getId());

  }

  @Test
  public void testFailingInput() throws IOException, CatalogTransformerException, ParseException, Exception {

    Metacard metacard = createTransformer().transform(new ByteArrayInputStream(sampleNMEAString2()
            .getBytes()));

    assertEquals("FEDERAL ST LAURENT(8PNN)", metacard.getTitle());

    WKTReader reader = new WKTReader();

    assertEquals(metacard.getLocation(), null);

  }


	protected void verifyBasics(Metacard metacard) {
		assertEquals(DEFAULT_TITLE, metacard.getTitle());
		assertEquals(DEFAULT_TYPE, metacard.getContentTypeName());
		assertEquals(DEFAULT_VERSION, metacard.getContentTypeVersion());
		assertEquals(sampleMetadata().replaceAll("\\s", ""), metacard.getMetadata().replaceAll("\\s", ""));
	}

  public static final String sampleNMEAString(){
    return "!AIVDM,2,1,2,A,5:LR1`000000iN3;KS58Tv0MD5aF22222222221?6p:5562:06T53p0T,0*68,rEXACTEARTH_ALL,1361887931\n" +
            "!AIVDM,2,2,2,A,p0Dp13PH1`88880,2*19,rEXACTEARTH_ALL,1361887931,1361898118";
  }

  public static final String sampleNMEAString2(){
    return "!AIVDM,2,1,9,A,54cPpV02;1G3Q0pr220HD@E84j1=B0h5E8DqB21?DpP;<4nB0K3CkU4Q@C8,0*4D,rEXACTEARTH_ALL,1361835781\n" +
            "!AIVDM,2,2,9,A,888888888880,2*15,rEXACTEARTH_ALL,1361835781,1361836800";
  }

  public static final String sampleNMEAString3(){
    return "!AIVDM,1,1,,A,15CEiP001`N`l4kmTNa38BIh0Gws,0*5C,rEXACTEARTH_ALL,1361835775,1361836800";
  }

  public static final String sampleMetadata(){
    return "<ddms:Resource xmlns:ddms=\"http://metadata.dod.mil/mdr/ns/DDMS/2.0/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:ICISM=\"urn:us:gov:ic:ism:v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metadata.dod.mil/mdr/ns/DDMS/2.0/ DDMS-v2_0.xsd\" xmlns:gml=\"http://www.opengis.net/gml\">\n" +
            "  <ddms:identifier ddms:qualifier=\"http://www.nmea.org/\" ddms:value=\"AISVDO\"/>\n" +
            "  <ddms:title  ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\" > 701006240 </ddms:title>\n" +
            "  <ddms:publisher ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\">\n" +
            "   <ddms:Organization>\n" +
            "    <ddms:name>RadiantBlue</ddms:name>\n" +
            "   </ddms:Organization>\n" +
            "  </ddms:publisher>\n" +
            "  <ddms:subjectCoverage>\n" +
            "   <ddms:Subject>\n" +
            "    <ddms:category ddms:code=\"null\" ddms:qualifier=\"http://www.nmea.org/\" ddms:label=\"SpeedOverGround\"/>\n" +
            "    <ddms:category ddms:code=\"null\" ddms:qualifier=\"http://www.nmea.org/\" ddms:label=\"TrueHeading\"/>\n" +
            "   </ddms:Subject>\n" +
            "  </ddms:subjectCoverage>\n" +
            "  <ddms:geospatialCoverage>\n" +
            "\t\t<ddms:GeospatialExtent>\n" +
            "\t\t\t<ddms:boundingGeometry>\n" +
            "\t\t\t\t<!-- Multiple points in here if needed -->\n" +
            "\t\t\t\t<gml:Point srsName=\"EPSG:4326\" gml:id=\"ID_1\">\n" +
            "\t\t\t\t\t<gml:pos>0.0 0.0</gml:pos>\n" +
            "\t\t\t\t</gml:Point>\n" +
            "\t\t\t</ddms:boundingGeometry>\n" +
            "\t\t</ddms:GeospatialExtent>\n" +
            "\t</ddms:geospatialCoverage>\n" +
            "  <ddms:security ICISM:ownerProducer=\"USA\" ICISM:classification=\"U\" />\n" +
            " </ddms:Resource>";
  }
}
