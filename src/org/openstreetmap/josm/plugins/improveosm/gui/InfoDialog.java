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
package org.openstreetmap.josm.plugins.improveosm.gui;

import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class InfoDialog {

    private static boolean dofTipIsDisplayed = false;
    private static boolean locationTipIsDisplayed = false;


    /**
     * Displays editing tips for the user. The dialog window is displayed when the plugin enters from cluster view to
     * segment view.If the user suppressed the notification the editing tips will be displayed only when JOSM is
     * reinstalled.
     *
     * @param zoom the current zoom level
     */
    public synchronized void displayDirectionOfFlowEditTip(final int zoom) {
        if (!dofTipIsDisplayed) {
            final int maxZoom = Config.getInstance().getMaxClusterZoom();
            if (!PreferenceManager.getInstance().loadDirectionOfFlowTipSuppressFlag() && (zoom > maxZoom)) {
                dofTipIsDisplayed = true;
                final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                        buildTextPane(DirectionOfFlowGuiConfig.getInstance().getDlgTipTxt(),
                                Main.parent.getBackground()),
                        DirectionOfFlowGuiConfig.getInstance().getDlgTipTitle(), JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.NO_OPTION;
                PreferenceManager.getInstance().saveDirectionOfFlowTipSuppressFlag(flag);
                dofTipIsDisplayed = false;
            }
        }
    }

    /**
     * Displays location tips for the user.If the user suppressed the notification the editing tips will be displayed
     * only when JOSM is reinstalled.
     *
     */
    public synchronized void displayLocationButtonTip() {
        if (!locationTipIsDisplayed && !PreferenceManager.getInstance().loadLocationTipSuppressFlag()) {
            locationTipIsDisplayed = true;
            final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                    buildTextPane(GuiConfig.getInstance().getLocationBtnTipTxt(), Main.parent.getBackground()),
                    GuiConfig.getInstance().getLocationBtnTipLbl(), JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, null, null);
            final boolean flag = val == JOptionPane.YES_OPTION;
            PreferenceManager.getInstance().saveLocationTipSuppressFlag(flag);
            locationTipIsDisplayed = false;
        }
    }


    private JTextPane buildTextPane(final String txt, final Color background) {
        final JTextPane txtPane = GuiBuilder.buildTextPane(txt, false, "text/html");
        if (background != null) {
            txtPane.setBackground(background);
        }
        txtPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        txtPane.setCaretPosition(0);
        return txtPane;
    }
}