/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
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