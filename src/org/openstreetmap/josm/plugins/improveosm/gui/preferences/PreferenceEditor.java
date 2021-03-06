/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.preferences;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the preference editor settings.
 *
 * @author Beata
 * @version $Revision$
 */
public class PreferenceEditor extends DefaultTabPreferenceSetting {

    private final PreferencePanel pnlPreference;


    /**
     * Builds a new preference editor, displaying settings for the available data layers.
     */
    public PreferenceEditor() {
        super(IconConfig.getInstance().getPluginIconName(), GuiConfig.getInstance().getPluginName(),
                GuiConfig.getInstance().getPluginTxt());
        pnlPreference = new PreferencePanel();
    }


    @Override
    public void addGui(final PreferenceTabbedPane pnlParent) {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(pnlPreference, BorderLayout.NORTH);
        createPreferenceTabWithScrollPane(pnlParent, mainPanel);
    }

    @Override
    public boolean ok() {
        final LocationPref locationPref = pnlPreference.selectedLocationPref();

        if (locationPref.equals(LocationPref.CUSTOM_SITE)) {
            final String value = pnlPreference.selectedCustomUrl();
            if (verifyURL(value)) {
                PreferenceManager.getInstance().saveLocationPrefValue(value);
                PreferenceManager.getInstance().saveLocationPrefOption(locationPref);
            } else {
                PreferenceManager.getInstance().saveLocationPrefOption(LocationPref.COPY_LOCATION);
                JOptionPane.showMessageDialog(pnlPreference,
                        "\"" + value + "\"" + GuiConfig.getInstance().getWrongSiteText(),
                        GuiConfig.getInstance().getWrongSiteTitle(), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            PreferenceManager.getInstance().saveLocationPrefOption(locationPref);
        }
        return false;
    }

    private boolean verifyURL(final String url) {
        final String[] patterns = Config.getInstance().getLocationPrefUrlPatterns();
        for (final String pattern : patterns) {
            if (url.contains(pattern)) {
                try {
                    if (((HttpURLConnection) new URL(url).openConnection())
                            .getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        return false;
                    }
                } catch (final IOException e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}