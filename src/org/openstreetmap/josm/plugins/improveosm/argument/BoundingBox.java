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
package org.openstreetmap.josm.plugins.improveosm.argument;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;


/**
 * Defines the attributes of a bounding box.
 *
 * @author Beata
 * @version $Revision$
 */
public class BoundingBox {

    /* longitude interval limits */
    private static final double MIN_LON = -180.0;
    private static final double MAX_LON = 180.0;

    /* latitude interval limits */
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LAT = 90.0;

    private double north;
    private double south;
    private double east;
    private double west;


    /**
     * Builds a new bounding box based on the given map view.
     *
     * @param mapView the current {@code MapView}
     */
    public BoundingBox(final MapView mapView) {
        final Bounds bounds =
                new Bounds(mapView.getLatLon(0, mapView.getHeight()), mapView.getLatLon(mapView.getWidth(), 0));
        setNorth(bounds.getMax().lat());
        setSouth(bounds.getMin().lat());
        setEast(bounds.getMax().lon());
        setWest(bounds.getMin().lon());
    }

    /**
     * Builds a bounding box with the given arguments. Latitude values should belong to the interval [-90.0,90.0].
     * Longitude values should belong to the interval [-180.0,180.0]. Invalid values will be normalized.
     *
     * @param north the northern border, given as decimal degrees
     * @param south the southern border, given as decimal degrees
     * @param east the eastern border, given as decimal degrees
     * @param west the western border, given as decimal degrees
     */
    public BoundingBox(final double north, final double south, final double east, final double west) {
        setNorth(north);
        setSouth(south);
        setEast(east);
        setWest(west);
    }


    public double getNorth() {
        return north;
    }

    private void setNorth(final double north) {
        this.north = north > MAX_LAT ? MAX_LAT : north;
    }

    public double getSouth() {
        return south;
    }

    private void setSouth(final double south) {
        this.south = south < MIN_LAT ? MIN_LAT : south;
    }

    public double getEast() {
        return east;
    }

    private void setEast(final double east) {
        this.east = east > MAX_LON ? MAX_LON : east;
    }

    public double getWest() {
        return west;
    }

    private void setWest(final double west) {
        this.west = west < MIN_LON ? MIN_LON : west;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int bit = 32;
        int result = 1;
        long temp = Double.doubleToLongBits(east);
        result = prime * result + (int) (temp ^ (temp >>> bit));
        temp = Double.doubleToLongBits(north);
        result = prime * result + (int) (temp ^ (temp >>> bit));
        temp = Double.doubleToLongBits(south);
        result = prime * result + (int) (temp ^ (temp >>> bit));
        temp = Double.doubleToLongBits(west);
        result = prime * result + (int) (temp ^ (temp >>> bit));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof BoundingBox) {
            final BoundingBox other = (BoundingBox) obj;
            result = Double.doubleToLongBits(east) == Double.doubleToLongBits(other.getEast());
            result = result && Double.doubleToLongBits(north) == Double.doubleToLongBits(other.getNorth());
            result = result && Double.doubleToLongBits(south) == Double.doubleToLongBits(other.getSouth());
            result = result && Double.doubleToLongBits(west) == Double.doubleToLongBits(other.getWest());
        }
        return result;
    }
}