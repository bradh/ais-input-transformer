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


import ddf.catalog.data.Metacard;
import ddf.catalog.data.MetacardImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.InputTransformer;

import java.io.InputStream;
import java.io.IOException;


public class AISInputTransformer implements InputTransformer  {

    @Override
    public Metacard transform(InputStream inputStream) throws IOException, CatalogTransformerException {
      return transform(inputStream, null);
    }

    @Override
    public Metacard transform(InputStream inputStream, String id) throws IOException, CatalogTransformerException{
      Metacard metacard = new MetacardImpl();

      return metacard;
    }
}
