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

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumSet;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the UI components of the preference settings.
 *
 * @author Beata
 * @version $Revision$
 */
class PreferencePanel extends JPanel {

    private static final long serialVersionUID = -5573686133711742041L;

    /* UI components */
    private JCheckBox cbMissingGeometry;
    private JCheckBox cbDirectionOfFlow;


    PreferencePanel() {
        super(new GridBagLayout());
        createComponents();
    }


    private void createComponents() {
        final EnumSet<DataLayer> dataLayers = PreferenceManager.getInstance().loadDataLayers();
        final Font font = getFont().deriveFont(Font.PLAIN);
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPreferenceLbl(), font, null), Constraints.LBL_DATA_LAYER);
        cbMissingGeometry =
                GuiBuilder.buildCheckBox(MissingGeometryGuiConfig.getInstance().getLayerTxt(), null, getBackground());
        cbMissingGeometry.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent event) {
                System.out.println(cbMissingGeometry.isSelected());
            }

        });
        if (dataLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            cbMissingGeometry.setSelected(true);
        }
        add(cbMissingGeometry, Constraints.CB_MISSING_GEOMETRY);
        cbDirectionOfFlow =
                GuiBuilder.buildCheckBox(DirectionOfFlowGuiConfig.getInstance().getLayerTxt(), null, getBackground());
        if (dataLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            cbDirectionOfFlow.setSelected(true);
        }
        add(cbDirectionOfFlow, Constraints.CB_DIRECTION_OF_FLOW);
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
        if (result.isEmpty()) {
            result.add(DataLayer.NONE);
        }
        return result;
    }
}