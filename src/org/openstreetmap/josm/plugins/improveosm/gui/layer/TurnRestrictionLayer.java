/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the TurnRestrictionLayer main functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionLayer extends ImproveOsmLayer<TurnRestriction> {

    /**
     * Builds a new TurnRestriction layer.
     */
    public TurnRestrictionLayer() {
        super(TurnRestrictionGuiConfig.getInstance().getLayerName(), new TurnRestrictionPaintHandler());
    }

    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getTurnRestrictonLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return TurnRestrictionGuiConfig.getInstance().getLayerTlt();
    }

    @Override
    TurnRestriction nearbyItem(final Point point) {
        return Util.nearbyTurnRestriction(getDataSet().getItems(), point);
    }

    @Override
    void updateSelectedItems() {
        final List<TurnRestriction> newList = new ArrayList<>();
        for (final TurnRestriction item : this.getSelectedItems()) {
            if (getDataSet().getItems().contains(item)) {
                if (item.getTurnRestrictions() == null) {
                    newList.add(item);
                } else {
                    final int idx = getDataSet().getItems().indexOf(item);
                    final TurnRestriction newItem = getDataSet().getItems().get(idx);
                    newList.add(newItem);
                }
            }
            setSelectedItems(newList);
        }
    }

    @Override
    BasicFilterDialog getFilterDialog() {
        return new TurnRestrictionFilterDialog();
    }

    @Override
    AbstractAction getDeleteAction() {
        return new TurnRestrictionLayerAction();
    }

    @Override
    public List<TurnRestriction> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox,
            final boolean multiSelected) {
        List<TurnRestriction> result;
        if (multiSelected && getSelectedItems().stream()
                .anyMatch(turnRestriction -> turnRestriction.getTurnRestrictions() != null)) {
            result = new ArrayList<>();
        } else {
            result = getDataSet().getItems().stream()
                    .filter(turnRestriction -> boundingBox.contains(turnRestriction.getPoint().getX(),
                            turnRestriction.getPoint().getY()) && turnRestriction.getTurnRestrictions() == null)
                    .collect(Collectors.toList());
        }
        return result;
    }


    private static class TurnRestrictionLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveTurnRestrictionLayerOpenedFlag(false);
        }
    }
}