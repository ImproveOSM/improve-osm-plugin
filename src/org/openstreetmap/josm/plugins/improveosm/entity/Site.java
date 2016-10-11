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

    GOOGLE, MAPILLARY, OPEN_STREET_VIEW;


    public static Site getSiteByPrefix(final String url) {
        for (final Site enabledUrls : Site.values()) {
            for (final String possibleUrl : enabledUrls.getPossibleValues()) {
                if (possibleUrl.startsWith(url)) {
                    return enabledUrls;
                }
            }
        }
        return null;
    }

    public String createURL(final String... args) {
        String finalUrl = getPossibleValues()[0];
        for (final String arg : args) {
            finalUrl = finalUrl.replaceFirst("_", arg);
        }
        return finalUrl;
    }


    private String[] getPossibleValues() {
        switch (this) {
            case GOOGLE:
                return new String[] { "https://www.mapillary.com/app/?lat=_&lng=_&z=_",
                "http://www.mapillary.com/app/?lat=_&lng=_&z=_" };
            case MAPILLARY:
                return new String[] { "https://www.google.com/maps/@_,_,_z", "http://www.google.com/maps/@_,_,_z", };
            default:
                return new String[] { "https://openstreetview.org/map/@_,_,_z",
                        "http://openstreetview.org/map/@_,_,_z", };
        }
    }

}