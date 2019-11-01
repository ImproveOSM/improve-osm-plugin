/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class InfoDialog {

    private static boolean dofTipIsDisplayed = false;
    private static boolean locationTipIsDisplayed = false;
    private static final String CONTENT_TYPE = "text/html";


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
                final int val = JOptionPane.showOptionDialog(MainApplication.getMap(),
                        TextComponentBuilder.buildTextPane(DirectionOfFlowGuiConfig.getInstance().getDlgTipTxt(),
                                CONTENT_TYPE, MainApplication.getMainFrame().getBackground(), false),
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
            final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView,
                    TextComponentBuilder.buildTextPane(GuiConfig.getInstance().getLocationBtnTipTxt(), CONTENT_TYPE,
                            MainApplication.getMainFrame().getBackground(), false),
                    GuiConfig.getInstance().getLocationBtnTipLbl(), JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, null, null);
            final boolean flag = val == JOptionPane.YES_OPTION;
            PreferenceManager.getInstance().saveLocationTipSuppressFlag(flag);
            locationTipIsDisplayed = false;
        }
    }
}