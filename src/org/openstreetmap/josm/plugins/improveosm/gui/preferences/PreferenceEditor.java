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
package org.openstreetmap.josm.plugins.improveosm.gui.preferences;

import java.awt.BorderLayout;
import java.util.EnumSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.entity.Site;
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
        final EnumSet<DataLayer> selectedDataLayers = pnlPreference.selectedDataLayers();
        PreferenceManager.getInstance().saveDataLayers(selectedDataLayers);

        final LocationPref locationPref = pnlPreference.selectedLocationPrefOption();

        if (locationPref.equals(LocationPref.CUSTOM_SITE)) {
            final String value = pnlPreference.selectedCustomUrl();
            if (Site.getSiteByPrefix(value) != null) {
                PreferenceManager.getInstance().saveLocationPrefValue(value);
                PreferenceManager.getInstance().saveLocationPrefOption(locationPref);
            } else {
                PreferenceManager.getInstance().saveLocationPrefOption(LocationPref.COPY_LOCATION);
                JOptionPane.showMessageDialog(pnlPreference, GuiConfig.getInstance().getWrongSiteText(),
                        GuiConfig.getInstance().getWrongSiteTitle(), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            PreferenceManager.getInstance().saveLocationPrefOption(locationPref);
        }
        return false;
    }

}