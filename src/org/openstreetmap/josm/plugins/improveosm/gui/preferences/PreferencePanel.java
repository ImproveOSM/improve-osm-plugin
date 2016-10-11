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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
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

    /* layer preference UI components */
    private JCheckBox cbMissingGeometry;
    private JCheckBox cbDirectionOfFlow;
    private JCheckBox cbTurnRestriction;

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
     * Returns the selected data layers.
     *
     * @return a set of {@code DataLayer}s
     */
    EnumSet<DataLayer> selectedDataLayers() {
        final EnumSet<DataLayer> result = EnumSet.noneOf(DataLayer.class);
        if (cbMissingGeometry.isSelected()) {
            result.add(DataLayer.MISSING_GEOMETRY);
        }
        if (cbDirectionOfFlow.isSelected()) {
            result.add(DataLayer.DIRECTION_OF_FLOW);
        }
        if (cbTurnRestriction.isSelected()) {
            result.add(DataLayer.TURN_RESTRICTION);
        }
        return result;
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
        createLayerPreferenceComponents();
        createLocationPreferenceComponents();
    }

    private void createLayerPreferenceComponents() {
        final EnumSet<DataLayer> dataLayers = PreferenceManager.getInstance().loadDataLayers();
        final EnumSet<DataLayer> enabledDataLayers = Config.getInstance().getEnabledDataLayers();
        final Font font = getFont().deriveFont(Font.PLAIN);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLayersPreferenceLbl(), font,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_DATA_LAYER);

        cbMissingGeometry = buildCheckBox(MissingGeometryGuiConfig.getInstance().getLayerTxt());
        if (!enabledDataLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            cbMissingGeometry.setEnabled(false);
        }
        if (dataLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            cbMissingGeometry.setSelected(true);
        }
        add(cbMissingGeometry, Constraints.CB_MISSING_GEOMETRY);

        cbDirectionOfFlow = buildCheckBox(DirectionOfFlowGuiConfig.getInstance().getLayerTxt());
        if (!enabledDataLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            cbDirectionOfFlow.setEnabled(false);
        }
        if (dataLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            cbDirectionOfFlow.setSelected(true);
        }
        add(cbDirectionOfFlow, Constraints.CB_DIRECTION_OF_FLOW);

        cbTurnRestriction = buildCheckBox(TurnRestrictionGuiConfig.getInstance().getLayerTxt());
        if (!enabledDataLayers.contains(DataLayer.TURN_RESTRICTION)) {
            cbTurnRestriction.setEnabled(false);
        }
        if (dataLayers.contains(DataLayer.TURN_RESTRICTION)) {
            cbTurnRestriction.setSelected(true);
        }
        add(cbTurnRestriction, Constraints.CB_TURN_RESTRICTION);
    }

    private JCheckBox buildCheckBox(final String text) {
        final JCheckBox cbbox = GuiBuilder.buildCheckBox(text, new JCheckBox().getFont().deriveFont(Font.PLAIN),
                new DataLayersSelectionAction(), false, false);
        cbbox.setBackground(getBackground());
        return cbbox;
    }

    private void createLocationPreferenceComponents() {
        final LocationPref savedLocationPref = PreferenceManager.getInstance().loadLocationPrefOption();
        final EnumSet<LocationPref> enabledLocationPref = Config.getInstance().getEnabledLocationPref();

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLocationPreferenceLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_LOCATION);

        final ButtonGroup buttonsGroup = new ButtonGroup();
        if (enabledLocationPref.contains(LocationPref.OPEN_STREET_VIEW)) {
            rbImproveOsmPage = buildRadioButton(GuiConfig.getInstance().getLocationPrefOpenStreetMap(),
                    savedLocationPref, LocationPref.OPEN_STREET_VIEW);
            buttonsGroup.add(rbImproveOsmPage);
            add(rbImproveOsmPage, Constraints.BTN_IMPROVE_OSM);
        }

        if (enabledLocationPref.contains(LocationPref.CUSTOM_SITE)) {
            final JPanel customPagePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
            rbCustomPage = buildRadioButton(GuiConfig.getInstance().getLocationPrefCustom(), savedLocationPref,
                    LocationPref.CUSTOM_SITE);
            customPagePanel.add(rbCustomPage);
            buttonsGroup.add(rbCustomPage);
            if ((savedLocationPref != null) && (savedLocationPref.equals(LocationPref.CUSTOM_SITE))) {
                txtCustomUrl = new JTextField(PreferenceManager.getInstance().loadLocationPrefValue(), URL_DIM);
            } else {
                txtCustomUrl = new JTextField(null, URL_DIM);
            }
            customPagePanel.add(txtCustomUrl);
            add(customPagePanel, Constraints.BTN_CUSTOM);
        }

        if (enabledLocationPref.contains(LocationPref.COPY_LOCATION)) {
            rbCopyLocation = buildRadioButton(GuiConfig.getInstance().getLocationPrefCopyLoc(), savedLocationPref,
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


    private final class DataLayersSelectionAction implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            final JCheckBox source = (JCheckBox) event.getSource();
            if (!source.isSelected() && !cbMissingGeometry.isSelected() && !cbDirectionOfFlow.isSelected()
                    && !cbTurnRestriction.isSelected()) {
                JOptionPane.showMessageDialog(PreferencePanel.this, GuiConfig.getInstance().getTxtDataLayerSettings());
                final EnumSet<DataLayer> enabledDataLayers = Config.getInstance().getEnabledDataLayers();
                if (enabledDataLayers.contains(DataLayer.MISSING_GEOMETRY)) {
                    cbMissingGeometry.setSelected(true);
                }
                if (enabledDataLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
                    cbDirectionOfFlow.setSelected(true);
                }
                if (enabledDataLayers.contains(DataLayer.TURN_RESTRICTION)) {
                    cbTurnRestriction.setSelected(true);
                }
            }
        }
    }
}