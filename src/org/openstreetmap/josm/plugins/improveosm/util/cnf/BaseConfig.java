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
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * Defines basic methods for loading runtime properties.
 *
 * @author Beata
 * @version $Revision$
 */
class BaseConfig {

    private static final String SEPARATOR = ",";
    private final Properties properties;


    /**
     * Builds a new configuration object for the given configuration file.
     *
     * @param fileName the configuration file name
     */
    BaseConfig(final String fileName) {
        final URL url = BaseConfig.class.getResource("/" + fileName);
        if (url == null) {
            // no need to catch this error, it is handled by JOSM error
            // mechanism
            throw new ExceptionInInitializerError("Could not find configuration file:" + fileName);
        }
        properties = new Properties();
        try (InputStream stream = url.openStream()) {
            properties.load(stream);
        } catch (final IOException e) {
            // no need to catch this error, it is handled by JOSM error
            // mechanism
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns a set of keys contained in the properties map.
     *
     * @return a set of objects
     */
    Set<Object> keySet() {
        return properties.keySet();
    }

    /**
     * Reads a list of properties associated with the given key.
     *
     * @param key a {@code String}
     * @return
     */
    List<Double> readProperties(final String key) {
        final String values = properties.getProperty(key);
        final List<Double> valueList = new ArrayList<>();
        if (values != null && !values.isEmpty()) {
            for (final String value : values.split(SEPARATOR)) {
                valueList.add(Double.parseDouble(value));
            }
        }
        return valueList;
    }

    /**
     * Reads the value of the property defined by the given key.
     *
     * @param key a {@code String}
     * @return a {@code String}
     */
    String readProperty(final String key) {
        return properties.getProperty(key);
    }
}