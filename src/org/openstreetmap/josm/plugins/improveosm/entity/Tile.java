/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the attributes of the 'Tile' business entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class Tile {

    private final Integer x;
    private final Integer y;
    private Long timestamp;
    private Integer numberOfTrips;
    private List<LatLon> points;
    private Status status;
    private TileType type;


    /**
     * Builds a new object with the given arguments.
     *
     * @param x the X identifier of a tile
     * @param y the Y identifier of a tile
     */
    public Tile(final Integer x, final Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Builds a new object with the given arguments.
     *
     * @param x the X identifier of a tile
     * @param y the Y identifier of a tile
     * @param timestamp the time of the last change on the tile
     * @param numberOfTrips the number of tiles that passed through the tile
     * @param points the list of missing points
     * @param status the status of the tile
     * @param type the type of the tile
     */
    public Tile(final Integer x, final Integer y, final Long timestamp, final Integer numberOfTrips,
            final List<LatLon> points, final Status status, final TileType type) {
        this(x, y);
        this.timestamp = timestamp;
        this.numberOfTrips = numberOfTrips;
        this.points = points;
        this.status = status;
        this.type = type;
    }


    public Integer getNumberOfTrips() {
        return numberOfTrips;
    }

    public List<LatLon> getPoints() {
        return points;
    }

    public Status getStatus() {
        return status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TileType getType() {
        return type;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(x);
        result = prime * result + EntityUtil.hashCode(y);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Tile other = (Tile) obj;
            result = EntityUtil.bothNullOrEqual(x, other.getX());
            result = result && EntityUtil.bothNullOrEqual(y, other.getY());
        }
        return result;
    }

    @Override
    public String toString() {
        return x + " -" + y;
    }
}