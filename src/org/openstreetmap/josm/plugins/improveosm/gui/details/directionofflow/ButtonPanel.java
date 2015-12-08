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
package org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicButtonPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Utils;


/**
 * Defines the components of the button panel used in the {@code DirectionOfFlowDetailsDialog} window.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends BasicButtonPanel<RoadSegment> {

    private static final long serialVersionUID = 2035810847510862585L;

    private static final int ROWS = 1;
    private static final int COLS = 6;

    /* UI components */
    private final JButton btnLocation;


    /**
     * Builds a new button panel.
     */
    ButtonPanel() {
        super(ROWS, COLS, new DisplayFilterDialog());

        btnLocation = GuiBuilder.buildButton(new CopyLocationAction(), IconConfig.getInstance().getLocationIcon(),
                DirectionOfFlowGuiConfig.getInstance().getBtnLocationTlt(), true);

        add(btnLocation, 1);
    }

    @Override
    public void enablePanelActions() {
        if (getItem().getStatus() == Status.OPEN) {
            enablePanelActions(true, true, false, true);
        } else {
            enablePanelActions(true, false, true, false);
        }
    }


    /* displays the FilterDialog window */

    private static class DisplayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 1260997951329896682L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                final FilterDialog dialog = new FilterDialog();
                dialog.setVisible(true);

            }
        }
    }

    /* copies the selected segment's/current position to clipboard */

    private class CopyLocationAction extends AbstractAction {

        private static final long serialVersionUID = 5864772613263351452L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                LatLon latLon = null;
                if (getItem() != null) {
                    final int idx = Math.round(getItem().getPoints().size() / 2);
                    latLon = getItem().getPoints().get(idx);
                } else {
                    latLon = Main.map.mapView.getRealBounds().getCenter();
                }
                Utils.copyToClipboard(Formatter.formatLatLon(latLon));
            }
        }
    }
}