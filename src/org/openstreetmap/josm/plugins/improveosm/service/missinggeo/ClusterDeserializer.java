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
package org.openstreetmap.josm.plugins.improveosm.service.missinggeo;

import java.lang.reflect.Type;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.Cluster;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 *
 * @author Beata
 * @version $Revision$
 */
class ClusterDeserializer implements JsonDeserializer<Cluster> {

    private static final String POINT = "point";
    private static final String NUMBER_OF_POINTS = "numberOfPoints";


    @Override
    public Cluster deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject obj = jsonElement.getAsJsonObject();
        final JsonObject pointObj = (JsonObject) obj.get(POINT);
        final LatLon latLon = context.deserialize(pointObj, LatLon.class);
        final int numberOfPoints = obj.get(NUMBER_OF_POINTS).getAsInt();
        return new Cluster(latLon, numberOfPoints);
    }
}