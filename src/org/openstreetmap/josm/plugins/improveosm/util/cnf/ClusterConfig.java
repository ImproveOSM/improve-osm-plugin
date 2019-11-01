/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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
        return map.containsKey(zoom) ? map.get(zoom) : Arrays.asList(DEF_RADIUSES);
    }
}