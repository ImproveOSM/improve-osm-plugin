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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.telenav.josm.common.entity.Status;
import com.telenav.josm.common.http.ContentType;
import com.telenav.josm.common.http.HttpConnector;
import com.telenav.josm.common.http.HttpConnectorException;


/**
 * Provides methods commonly used by the {@code Service} implementations.
 *
 * @author Beata
 * @version $Revision$
 */
public class BaseService {

    private final Gson gson = createGsonBuilder().create();


    /**
     * Creates a {@code GsonBuilder} used for parsing the response of the service. A specific service class need to
     * override this method, if custom deserializers needs to be registered.
     *
     * @return a {@code GsonBuilder} object
     */
    private GsonBuilder createGsonBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LatLon.class, new LatLonDeserializer());
        return builder;
    }

    /**
     * Executes a HTTP GET method and returns the service response. The response is transformed to the specified type.
     *
     * @param url represents the service URL
     * @param responseType represents the response type
     * @return a {@code T} object
     * @throws ServiceException if the operation fail
     */
    public <T> T executeGet(final String url, final Class<T> responseType) throws ServiceException {
        String response;
        try {
            response = new HttpConnector(url).get();
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    /**
     * Executes a HTTP POST method and reads the service response. The response is transformed to the specified type.
     *
     * @param url represents the service URL
     * @param content represents the request's body
     * @param responseType represents the response type
     * @return a {@code T} object
     * @throws ServiceException if the operation failed
     */
    public <T> T executePost(final String url, final String content, final Class<T> responseType)
            throws ServiceException {
        String response;
        try {
            response = new HttpConnector(url).post(content, ContentType.JSON);
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    /**
     * Builds a JSON representing the body of a HTTP request.
     *
     * @param request the object that needs to be sent
     * @param requestType the type of the object
     * @return a {@code String} containing the corresponding JSON
     */
    public <T> String buildRequest(final T request, final Class<T> requestType) {
        return gson.toJson(request, requestType);
    }

    /**
     * Verifies if the status is {@code HttpURLConnection.HTTP_OK}. If not the method throws an exception.
     *
     * @param status a {@code ResponseStatus} represents the status of a service response
     * @throws ServiceException containing the status API message
     */
    public void verifyResponseStatus(final Status status) throws ServiceException {
        if (status != null && status.getHttpCode() != HttpURLConnection.HTTP_OK) {
            throw new ServiceException(status.getApiMessage());
        }
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
}