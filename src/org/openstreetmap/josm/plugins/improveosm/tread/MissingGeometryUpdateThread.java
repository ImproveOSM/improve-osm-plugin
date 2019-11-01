/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.tread;

import org.openstreetmap.josm.plugins.improveosm.ServiceHandler;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public final class MissingGeometryUpdateThread extends UpdateThread<Tile> {

    public MissingGeometryUpdateThread(final ImproveOsmDetailsDialog detailsDialog,
            final ImproveOsmLayer<Tile> layer) {
        super(detailsDialog, layer);
    }

    @Override
    DataSet<Tile> searchData(final BoundingBox bbox, final int zoom) {
        final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
        return ServiceHandler.getMissingGeometryHandler().search(bbox, filter, zoom);
    }

    @Override
    boolean shouldClearSelection(final Tile item) {
        final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
        boolean result = filter.getStatus() == item.getStatus();
        result = result && (filter.getTypes() == null || filter.getTypes().contains(item.getType()));
        result = result && (filter.getCount() == null || filter.getCount() <= item.getNumberOfTrips());
        return !result;
    }
}