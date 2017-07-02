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
    List<TurnRestriction> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox) {
        return getDataSet().getItems().stream()
                .filter(turnRestriction -> Util.turnRestrictionIsInsideBoundingBox(turnRestriction, boundingBox))
                .collect(Collectors.toList());
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
            setSelectedItems(newList);  // why each time
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


    private static class TurnRestrictionLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveTurnRestrictionLayerOpenedFlag(false);
        }
    }
}