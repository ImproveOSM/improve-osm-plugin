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
import java.util.EnumSet;
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
    private JRadioButton rbCopyLocation;


    PreferencePanel() {
        super(new GridBagLayout());
        createComponents();
    }

    /**
     * Returns the selected location button settings.
     *
     * @return a {@code LocationPref}s
     */
    LocationPref selectedLocationPrefOption() {
        if (rbImproveOsmPage.isSelected()) {
            return LocationPref.OPEN_STREET_VIEW;
        }
        if (rbCustomPage.isSelected()) {
            return LocationPref.CUSTOM_SITE;
        }
        return LocationPref.COPY_LOCATION;
    }

    String selectedCustomUrl() {
        return txtCustomUrl.getText();
    }

    private void createComponents() {
        createLocationPreferenceComponents();
    }

    private void createLocationPreferenceComponents() {
        final LocationPref savedLocationPref = PreferenceManager.getInstance().loadLocationPrefOption();
        final EnumSet<LocationPref> enabledLocationPref = Config.getInstance().getEnabledLocationPref();

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLocationPreferenceLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_LOCATION);

        final ButtonGroup buttonsGroup = new ButtonGroup();
        if (enabledLocationPref.contains(LocationPref.OPEN_STREET_VIEW)) {
            final boolean isSelected =
                    (savedLocationPref != null) && (savedLocationPref.equals(LocationPref.OPEN_STREET_VIEW));
            rbImproveOsmPage = GuiBuilder.buildRadioButton(GuiConfig.getInstance().getLocationPrefOpenStreetMap(),
                    Font.PLAIN, getBackground(), isSelected);
            buttonsGroup.add(rbImproveOsmPage);
            add(rbImproveOsmPage, Constraints.BTN_IMPROVE_OSM);
        }

        if (enabledLocationPref.contains(LocationPref.CUSTOM_SITE)) {
            final boolean isSelected =
                    (savedLocationPref != null) && (savedLocationPref.equals(LocationPref.CUSTOM_SITE));
            rbCustomPage = GuiBuilder.buildRadioButton(GuiConfig.getInstance().getLocationPrefCustom(), Font.PLAIN,
                    getBackground(), isSelected);

            buttonsGroup.add(rbCustomPage);
            if ((savedLocationPref != null) && (savedLocationPref.equals(LocationPref.CUSTOM_SITE))) {
                txtCustomUrl = GuiBuilder.buildTextField(PreferenceManager.getInstance().loadLocationPrefValue(),
                        Font.BOLD, getBackground(), URL_DIM);
            } else {
                txtCustomUrl = GuiBuilder.buildTextField(null, Font.BOLD, getBackground(), URL_DIM);
            }

            final JPanel customPagePanel =
                    GuiBuilder.buildFlowLayoutPanel(FlowLayout.LEADING, rbCustomPage, txtCustomUrl);
            add(customPagePanel, Constraints.BTN_CUSTOM);
        }

        if (enabledLocationPref.contains(LocationPref.COPY_LOCATION)) {
            rbCopyLocation = buildRadioButton(GuiConfig.getInstance().getCopyLocationPrefLbl(), savedLocationPref,
                    LocationPref.COPY_LOCATION);
            if (savedLocationPref == null) {
                rbCopyLocation.setSelected(true);
            }
            buttonsGroup.add(rbCopyLocation);
            add(rbCopyLocation, Constraints.BTN_COPY_LOC);
        }
    }

    private JRadioButton buildRadioButton(final String label, final LocationPref savedPref, final LocationPref pref) {
        final JRadioButton rbuttom = new JRadioButton(label);
        if ((savedPref != null) && (savedPref.equals(pref))) {
            rbuttom.setSelected(true);
        }
        return rbuttom;
    }
}