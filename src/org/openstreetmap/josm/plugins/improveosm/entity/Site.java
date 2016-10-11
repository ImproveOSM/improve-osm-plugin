/*
 *  Copyright 2016 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.improveosm.entity;

/**
 * An enum containing enabled sites for the location setting.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum Site {

    GOOGLE {

        @Override
        public String toString() {
            return "http://www.mapillary.com/app/?lat=_&lng=_&z=_";
        }
    },

    MAPILLARY {

        @Override
        public String toString() {
            return "http://www.google.com/maps/@_,_,_z";
        }
    },

    OPEN_STREET_VIEW {

        @Override
        public String toString() {
            return "http://openstreetview.org/map/@_,_,_z";
        }
    };

    public static Site getSiteByPrefix(final String url) {
        for (final Site enabledUrl : Site.values()) {
            if (enabledUrl.toString().startsWith(url)) {
                return enabledUrl;
            }
        }
        return null;
    }

    public String createURL(final String... args) {
        String finalUrl = toString();
        for (final String arg : args) {
            finalUrl = finalUrl.replaceFirst("_", arg);
        }
        return finalUrl;
    }

}