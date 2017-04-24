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

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the UI components of the preference settings.
 *
 * @author Beata
 * @version $Revision$
 */
class PreferencePanel extends JPanel {

    private static final long serialVersionUID = -5573686133711742041L;

    private static final int URL_DIM = 20;

    /* location preference UI components */
    private JRadioButton rbImproveOsmPage;
    private JRadioButton rbCustomPage;
    private JTextField txtCustomUrl;


    PreferencePanel() {
        super(new GridBagLayout());
        createComponents();
    }


    private void createComponents() {
        final LocationPref savedLocationPref = PreferenceManager.getInstance().loadLocationPrefOption();
        final Set<LocationPref> enabledLocationPref = Config.getInstance().getEnabledLocationPref();

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLocationPreferenceLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_LOCATION);

        final ButtonGroup buttonsGroup = new ButtonGroup();
        if (enabledLocationPref.contains(LocationPref.OPEN_STREET_VIEW)) {
            final boolean isSelected =
                    savedLocationPref != null && savedLocationPref.equals(LocationPref.OPEN_STREET_VIEW);
            rbImproveOsmPage = GuiBuilder.buildRadioButton(GuiConfig.getInstance().getLocationPrefOpenStreetMap(),
                    Font.PLAIN, getBackground(), isSelected);
            buttonsGroup.add(rbImproveOsmPage);
            add(rbImproveOsmPage, Constraints.BTN_IMPROVE_OSM);
        }

        if (enabledLocationPref.contains(LocationPref.CUSTOM_SITE)) {
            final boolean isSelected = savedLocationPref != null && savedLocationPref.equals(LocationPref.CUSTOM_SITE);
            rbCustomPage = GuiBuilder.buildRadioButton(GuiConfig.getInstance().getLocationPrefCustom(), Font.PLAIN,
                    getBackground(), isSelected);
            buttonsGroup.add(rbCustomPage);
            txtCustomUrl = GuiBuilder.buildTextField(PreferenceManager.getInstance().loadLocationPrefValue(),
                    Font.PLAIN, getBackground(), URL_DIM);
            add(GuiBuilder.buildFlowLayoutPanel(FlowLayout.LEADING, rbCustomPage, txtCustomUrl),
                    Constraints.BTN_CUSTOM);
        }

        if (enabledLocationPref.contains(LocationPref.COPY_LOCATION)) {
            final boolean isSelected = savedLocationPref == null;
            final JRadioButton rbCopyLocation = GuiBuilder.buildRadioButton(
                    GuiConfig.getInstance().getCopyLocationPrefLbl(), Font.PLAIN, getBackground(), isSelected);

            buttonsGroup.add(rbCopyLocation);
            add(rbCopyLocation, Constraints.BTN_COPY_LOC);
        }
    }

    /**
     * Returns the selected location button settings.
     *
     * @return a {@code LocationPref}s
     */
    LocationPref selectedLocationPref() {
        LocationPref locationPref = LocationPref.COPY_LOCATION;
        if (rbImproveOsmPage.isSelected()) {
            locationPref = LocationPref.OPEN_STREET_VIEW;
        }
        if (rbCustomPage.isSelected()) {
            locationPref = LocationPref.CUSTOM_SITE;
        }
        return locationPref;
    }

    String selectedCustomUrl() {
        return txtCustomUrl.getText();
    }
}