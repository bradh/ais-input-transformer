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
	private static final String SAMPLE_ID = "701006240";
	public static final String DEFAULT_TITLE = "LW 268";
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

		assertThat(geometry.getCoordinate().x, is(0.0));

		assertThat(geometry.getCoordinate().y, is(0.0));

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
		assertEquals("<xml>701006240 LW 268 </xml>", metacard.getMetadata());
	}

  private static final String sampleNMEAString(){
    return "!AIVDM,2,1,2,A,5:LR1`000000iN3;KS58Tv0MD5aF22222222221?6p:5562:06T53p0T,0*68,rEXACTEARTH_ALL,1361887931\n" +
            "!AIVDM,2,2,2,A,p0Dp13PH1`88880,2*19,rEXACTEARTH_ALL,1361887931,1361898118";
  }

}
