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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import ddf.catalog.data.Metacard;
import ddf.catalog.transform.CatalogTransformerException;
import org.codice.ddf.transform.ais.AISInputTransformer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestAISInputTransformer {
	private static final String SAMPLE_ID = "367085430";
	public static final String DEFAULT_TITLE = "MELISSA B";
	public static final String DEFAULT_VERSION = null;
	public static final String DEFAULT_TYPE = "application/ais-nmea";

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
	public void testPointGeo() throws IOException, CatalogTransformerException, ParseException {

		Metacard metacard = new AISInputTransformer().transform(new ByteArrayInputStream(sampleNMEAString()
				.getBytes()));

		verifyBasics(metacard);

		WKTReader reader = new WKTReader();

		Geometry geometry = reader.read(metacard.getLocation());

		assertThat(geometry.getCoordinate().x, is(-117.3977567));

		assertThat(geometry.getCoordinate().y, is(32.80070167));

	}

  @Test
  public void testSetId() throws IOException, CatalogTransformerException {

    Metacard metacard = new AISInputTransformer().transform(new ByteArrayInputStream(sampleNMEAString()
            .getBytes()), SAMPLE_ID);

    verifyBasics(metacard);

    assertEquals(SAMPLE_ID + " doesn't equal " + metacard.getId(), SAMPLE_ID, metacard.getId());

  }

	protected void verifyBasics(Metacard metacard) {
		assertEquals(DEFAULT_TITLE, metacard.getTitle());
		assertEquals(DEFAULT_TYPE, metacard.getContentTypeName());
		assertEquals(DEFAULT_VERSION, metacard.getContentTypeVersion());
		assertEquals("<xml>MELISSA B</xml>", metacard.getMetadata());
	}

  private static final String sampleNMEAString(){
    return "367085430,0,731.3864841,4.6,32.80070167,-117.3977567,150.3,511,1330605281,0,MELISSA B,31,14,4,0,14,0,4,20,SAN DIEGO,WDC8339,0,-1,-1";
  }

}
