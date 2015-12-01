/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.improveosm.service;

import java.net.HttpURLConnection;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.service.entity.ResponseStatus;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpConnector;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpConnectorException;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class BaseService {

    private final Gson gson;

    public BaseService() {
        this.gson = createGsonBuilder().create();
    }

    public GsonBuilder createGsonBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LatLon.class, new LatLonDeserializer());
        return builder;
    }

    public <T> T executeGet(final String url, final Class<T> responseType) throws ServiceException {
        String response = null;
        try {
            response = new HttpConnector(url, HttpMethod.GET).read();
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    public <T> T executePost(final String url, final String content, final Class<T> responseType)
            throws ServiceException {
        String response = null;
        try {
            final HttpConnector connector = new HttpConnector(url, HttpMethod.POST);
            connector.write(content);
            response = connector.read();
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    private <T> T parseResponse(final String response, final Class<T> responseType) throws ServiceException {
        T root = null;
        if (response != null) {
            try {
                root = gson.fromJson(response, responseType);
            } catch (final JsonSyntaxException e) {
                throw new ServiceException(e);
            }
        }
        return root;
    }

    public <T> String buildRequest(final T request, final Class<T> requestType) {
        return gson.toJson(request, requestType);
    }

    public void verifyResponseStatus(final ResponseStatus status) throws ServiceException {
        if (status != null && status.getHttpCode() != HttpURLConnection.HTTP_OK) {
            throw new ServiceException(status.getApiMessage());
        }
    }
}