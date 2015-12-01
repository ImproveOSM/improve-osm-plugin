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
package org.openstreetmap.josm.plugins.improveosm.util.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A general HTTP connector that connects to a server and reads the content at the specified URL. This class is meant to
 * hide the details of HTTP connection setup, sending of the HTTP request, and receiving and interpretation of the HTTP
 * response.
 *
 * @author Beata
 * @version $Revision$
 */
public class HttpConnector {

    private static final int READ_TIMEOUT = 0;
    private static final int CONNECT_TIMEOUT = 10000;

    private HttpURLConnection connection;
    private final boolean connected = false;


    /**
     * Builds a new {@code HttpConnector} for the given URL and method.
     *
     * @param url the URL address to connect
     * @param method the HTTP method be be executed
     * @throws HttpConnectorException if an error occurred during the connection
     */
    public HttpConnector(final String url, final HttpMethod method) throws HttpConnectorException {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(method.name());
            connection.setDoOutput(true);
        } catch (final IOException e) {
            throw new HttpConnectorException(e);
        }
        if (method == HttpMethod.POST) {
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        }
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
    }

    /**
     * Reads the response from the URL. If the connection has not already been established connects to the URL. The
     * response type returned depends on the received response code. If the response code is:
     * <ul>
     * <li>{@link HttpURLConnection#HTTP_OK} the method returns the content of the input stream</li>
     * <li>otherwise the method returns the content of the error stream</li>
     * </ul>
     *
     * @return a {@code String} containing the response content
     * @throws HttpConnectorException if the input/error stream cannot be obtained or the content cannot be read
     */
    public String read() throws HttpConnectorException {
        if (!connected) {
            connect();
        }
        String response = null;
        try {
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = HttpUtil.readUtf8Content(connection.getInputStream());
            } else {
                response = HttpUtil.readUtf8Content(connection.getErrorStream());
            }
        } catch (final IOException e) {
            throw new HttpConnectorException(e);
        } finally {
            disconnect();
        }
        return response;
    }

    /**
     * Writes the given content to the message body using 'UTF-8' character encoding.
     *
     * @param json a {@code String} in JSON format
     * @throws HttpConnectorException if the output stream cannot be obtained or the content cannot be sent
     */
    public void write(final String json) throws HttpConnectorException {
        if (!connected) {
            connect();
        }
        if (json != null) {
            try (BufferedWriter out =
                    new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), HttpUtil.ENCODING))) {
                out.write(json);
            } catch (final IOException e) {
                throw new HttpConnectorException(e);
            } finally {
                disconnect();
            }
        }
    }

    private void connect() throws HttpConnectorException {
        try {
            connection.connect();
        } catch (final IOException e) {
            throw new HttpConnectorException(e);
        }
    }

    private void disconnect() throws HttpConnectorException {
        try {
            connection.disconnect();
        } catch (final Exception e) {
            throw new HttpConnectorException(e);
        }
    }
}