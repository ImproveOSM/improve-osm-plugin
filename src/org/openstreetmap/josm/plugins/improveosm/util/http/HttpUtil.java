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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.EnumSet;
import org.apache.commons.io.IOUtils;


/**
 * Holds utility methods.
 *
 * @author Beata
 * @version $Revision$
 */
public final class HttpUtil {

    public static final String ENCODING = "utf-8";

    private HttpUtil() {}


    /**
     * Encodes the given parameter using {@code HttpUtil#ENCODING} encoding.
     *
     * @param param the parameter to be encoded
     * @return the encoded parameter
     */
    public static String utf8Encode(final String content) {
        String encodedContent = null;
        try {
            encodedContent = URLEncoder.encode(content, ENCODING);
        } catch (final UnsupportedEncodingException ex) {
            /* should not appear since UTF-8 is a supported encoding */
        }
        return encodedContent;
    }

    /**
     * Reads the content of the given input stream and returns in string format.
     *
     * @param input a {@code InputStream} the stream which content will be read
     * @return a {@code String} containing the content of the input stream
     * @throws IOException if the reading operation failed
     */
    static String readUtf8Content(final InputStream input) throws IOException {
        String result;
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(input, writer, ENCODING);
            result = writer.toString();
        } finally {
            IOUtils.closeQuietly(input);
        }
        return result;
    }

    public static <E extends Enum<E>> String utf8Encode(final EnumSet<E> list) {
        final StringBuilder param = new StringBuilder();
        for (final Enum<E> item : list) {
            param.append(item.name()).append(",");
        }
        return utf8Encode(param.substring(0, param.length() - 1));
    }
}