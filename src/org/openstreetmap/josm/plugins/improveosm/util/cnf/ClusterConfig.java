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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Holds cluster radius configurations. The clusters are drawn to the map based on this configuration.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ClusterConfig extends BaseConfig {

    private static final ClusterConfig INSTANCE = new ClusterConfig();

    private static final String CONFIG_FILE = "improveosm_cluster.properties";
    private static final int DEF_COUNT = 4;
    private static final Double[] DEF_RADIUSES = { 20.0, 40.0, 60.0, 80.0 };

    private int count;
    private final Map<Integer, List<Double>> map;


    private ClusterConfig() {
        super(CONFIG_FILE);
        try {
            count = Integer.parseInt(readProperty("count"));
        } catch (final NumberFormatException e) {
            count = DEF_COUNT;
        }
        map = new HashMap<>();
        for (final Object key : keySet()) {
            final String keyStr = (String) key;
            if (keyStr.startsWith("zoom_")) {
                final String mapKeyStr = keyStr.replace("zoom_", "");
                final int mapKey = Integer.parseInt(mapKeyStr);
                final List<Double> list = readDoublePropertiesList(keyStr);
                map.put(mapKey, list);
            }
        }
    }


    public static ClusterConfig getInstance() {
        return INSTANCE;
    }

    public int getCount() {
        return count;
    }

    /**
     * Returns the radius list corresponding to he given zoom level.
     *
     * @param zoom a zoom level
     * @return a list of radiuses
     */
    public List<Double> getRadiusList(final int zoom) {
        List<Double> list;
        if (map.containsKey(zoom)) {
            list = map.get(zoom);
        } else {
            list = Arrays.asList(DEF_RADIUSES);
        }
        return list;
    }
}