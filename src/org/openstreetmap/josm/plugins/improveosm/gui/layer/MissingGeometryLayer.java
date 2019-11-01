/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the MissingGeometryLayer main functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryLayer extends ImproveOsmLayer<Tile> {

    /**
     * Builds a new MissingGeometry layer.
     */
    public MissingGeometryLayer() {
        super(MissingGeometryGuiConfig.getInstance().getLayerName(), new MissingGeometryPaintHandler());
    }


    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getMissingGeometryLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return MissingGeometryGuiConfig.getInstance().getLayerTlt();
    }

    @Override
    Tile nearbyItem(final Point point) {
        return Util.nearbyTile(getDataSet().getItems(), point);
    }

    @Override
    BasicFilterDialog getFilterDialog() {
        return new MissingGeometryFilterDialog();
    }

    @Override
    AbstractAction getDeleteAction() {
        return new DeleteMissingGeometryLayerAction();
    }

    @Override
    public List<Tile> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox, final boolean multiSelected) {
        return getDataSet().getItems().stream().filter(tile -> Util.tileIntersectsBoundingBox(tile, boundingBox))
                .collect(Collectors.toList());
    }

    private static class DeleteMissingGeometryLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(false);
        }
    }
}