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

import java.util.EnumSet;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class PreferenceEditor extends DefaultTabPreferenceSetting {

    private PreferencePanel pnlPreference;

    public PreferenceEditor() {
        super(IconConfig.getInstance().getPluginIconName(), GuiConfig.getInstance().getPluginName(),
                GuiConfig.getInstance().getPluginTxt());
    }

    @Override
    public void addGui(final PreferenceTabbedPane pnlParent) {
        pnlPreference = new PreferencePanel();
        createPreferenceTabWithScrollPane(pnlParent, pnlPreference);
    }

    @Override
    public boolean ok() {
        final EnumSet<DataLayer> selectedDataLayers = pnlPreference.selectedDataLayers();
        PreferenceManager.getInstance().saveDataLayers(selectedDataLayers);
        return false;
    }

}