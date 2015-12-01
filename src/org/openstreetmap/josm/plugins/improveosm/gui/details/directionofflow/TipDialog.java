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

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Helper class, displays an info dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class TipDialog {

    private static boolean isDisplayed = false;

    /**
     * Displays editing tips for the user. The dialog window is displayed when the plugin enters from cluster view to
     * segment view.If the user suppressed the notification the editing tips will be displayed only when JOSM is
     * reinstalled.
     *
     * @param zoom the current zoom level
     * @param prevZoom the previous zoom level
     */
    public synchronized void displayDialog(final int zoom) {
        if (!isDisplayed) {
            final int maxZoom = Config.getDirectionOfFlowInstance().getMaxClusterZoom();
            if (!PreferenceManager.getInstance().loadDirectionOfFlowTipSuppressFlag() && (zoom > maxZoom)) {
                isDisplayed = true;
                final String txt = DirectionOfFlowGuiConfig.getInstance().getDlgTipText().replace("iconPath",
                        IconConfig.getInstance().getTipIconPath().toString());
                final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                        GuiBuilder.buildTextPane(txt, Main.parent.getBackground()),
                        DirectionOfFlowGuiConfig.getInstance().getDlgTipTitle(), JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().saveDirectionOfFlowTipSuppressFlag(flag);
                isDisplayed = false;
            }
        }
    }
}