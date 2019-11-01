/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.tread;

import org.openstreetmap.josm.plugins.improveosm.ServiceHandler;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public final class TurnRestrictionUpdateThread extends UpdateThread<TurnRestriction> {

    public TurnRestrictionUpdateThread(final ImproveOsmDetailsDialog detailsDialog,
            final ImproveOsmLayer<TurnRestriction> layer) {
        super(detailsDialog, layer);
    }

    @Override
    DataSet<TurnRestriction> searchData(final BoundingBox bbox, final int zoom) {
        final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
        return ServiceHandler.getTurnRestrictionHandler().search(bbox, filter, zoom);
    }

    @Override
    boolean shouldClearSelection(final TurnRestriction item) {
        final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
        boolean result = filter.getConfidenceLevels() == null
                || filter.getConfidenceLevels().contains(item.getConfidenceLevel());
        result = result && filter.getStatus().equals(item.getStatus());
        return !result;
    }
}