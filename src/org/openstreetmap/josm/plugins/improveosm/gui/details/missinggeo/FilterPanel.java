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
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.EnumSet;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Displays the possible data filters: status, tile types, number of points/number of trips. For small zoom levels the
 * number of points filter is taken into considerations, for big zoom level the number of trips filter is taken into
 * consideration.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterPanel extends JPanel {

    private static final long serialVersionUID = -2111695621098772652L;

    /* UI components */
    private JRadioButton rbStatusOpen;
    private JRadioButton rbStatusSolved;
    private JRadioButton rbStatusInvalid;
    private ButtonGroup btnGroupStatus;
    private JCheckBox cbTypeParking;
    private JCheckBox cbTypeRoad;
    private JCheckBox cbTypeBoth;
    private JCheckBox cbIncludeWater;
    private JCheckBox cbIncludePath;
    private JTextField txtCount;


    /**
     * Builds a new filter panel.
     *
     * @param tileFilter specifies if the tile or cluster filters needs to be displayed
     */
    FilterPanel() {
        super(new GridBagLayout());

        final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();

        addStatusFilter(filter.getStatus());
        addTypesFilter(filter.getTypes());
        boolean includeWater = false;
        boolean includePath = false;
        if (filter.getTypes() != null) {
            includeWater = filter.getTypes().contains(TileType.WATER);
            includePath = filter.getTypes().contains(TileType.PATH);
        }
        addIncludeWaterFilter(includeWater);
        addIncludePathFilter(includePath);
        addCountFilter(filter.getCount());
    }


    private void addStatusFilter(final Status status) {
        add(GuiBuilder.buildLabel(getGuiCnf().getDlgFilterLblStatus(), getFont().deriveFont(Font.BOLD), null),
                Constraints.LBL_STATUS);
        rbStatusOpen = GuiBuilder.buildRadioButton(Status.OPEN.toString(), Status.OPEN.name(), getBackground());
        rbStatusSolved = GuiBuilder.buildRadioButton(Status.SOLVED.toString(), Status.SOLVED.name(), getBackground());
        rbStatusInvalid =
                GuiBuilder.buildRadioButton(Status.INVALID.toString(), Status.INVALID.name(), getBackground());
        btnGroupStatus = GuiBuilder.buildButtonGroup(rbStatusOpen, rbStatusSolved, rbStatusInvalid);
        selectStatus(status);
        add(rbStatusOpen, Constraints.RB_OPEN);
        add(rbStatusSolved, Constraints.RB_SOLVED);
        add(rbStatusInvalid, Constraints.RB_INVALID);
    }

    private void addTypesFilter(final EnumSet<TileType> types) {
        add(GuiBuilder.buildLabel(getGuiCnf().getDlgFilterLblType(), getFont().deriveFont(Font.BOLD), null),
                Constraints.LBL_TYPE);
        cbTypeParking =
                GuiBuilder.buildCheckBox(TileType.PARKING.displayValue(), TileType.PARKING.name(), getBackground());
        cbTypeRoad = GuiBuilder.buildCheckBox(TileType.ROAD.displayValue(), TileType.ROAD.name(), getBackground());
        cbTypeBoth = GuiBuilder.buildCheckBox(TileType.BOTH.displayValue(), TileType.BOTH.name(), getBackground());
        selectTypes(types);
        add(cbTypeParking, Constraints.RB_PARKING);
        add(cbTypeRoad, Constraints.RB_ROAD);
        add(cbTypeBoth, Constraints.RB_BOTH);
    }

    private void addIncludeWaterFilter(final boolean includeWater) {
        add(GuiBuilder.buildLabel(getGuiCnf().getDlgFilterLblWater(), getFont().deriveFont(Font.BOLD), null),
                Constraints.LBL_WATER);
        cbIncludeWater = GuiBuilder.buildCheckBox(getGuiCnf().getLblDisplay(), null, getBackground());
        cbIncludeWater.setSelected(includeWater);
        add(cbIncludeWater, Constraints.RB_WATER);
    }

    private void addIncludePathFilter(final boolean includePath) {
        add(GuiBuilder.buildLabel(getGuiCnf().getDlgFilterLblPedestrian(), getFont().deriveFont(Font.BOLD), null),
                Constraints.LBL_PEDESTRIAN);
        cbIncludePath = GuiBuilder.buildCheckBox(getGuiCnf().getLblDisplay(), null, getBackground());
        cbIncludePath.setSelected(includePath);
        add(cbIncludePath, Constraints.RB_PATH);
    }

    private void addCountFilter(final Integer value) {
        final String lblTxt;
        if (Util.zoom(Main.map.mapView.getRealBounds()) > Config.getMissingGeometryInstance().getMaxClusterZoom()) {
            lblTxt = MissingGeometryGuiConfig.getInstance().getLblTripCount();
        } else {
            lblTxt = MissingGeometryGuiConfig.getInstance().getLblPointCount();
        }
        add(GuiBuilder.buildLabel(lblTxt, getFont().deriveFont(Font.BOLD), null), Constraints.LBL_COUNT);
        final String valueStr = value != null ? value.toString() : "";
        txtCount = GuiBuilder.buildTextField(valueStr, getFont().deriveFont(Font.PLAIN), Color.white);
        txtCount.setInputVerifier(
                new PositiveIntegerVerifier(txtCount, GuiConfig.getInstance().getTxtInvalidInteger()));
        add(txtCount, Constraints.TXT_COUNT);
    }

    /**
     * Resets the filters to it's initial state.
     */
    void resetFilters() {
        selectStatus(MissingGeometryFilter.DEFAULT.getStatus());
        selectTypes(MissingGeometryFilter.DEFAULT.getTypes());
        cbIncludeWater.setSelected(false);
        cbIncludePath.setSelected(false);
        final String txt = MissingGeometryFilter.DEFAULT.getCount() != null
                ? MissingGeometryFilter.DEFAULT.getCount().toString() : "";
                txtCount.setText(txt);
                txtCount.setBackground(Color.white);
    }

    /**
     * Returns the filters set by the user.
     *
     * @return a {@code SearchFilter} object
     */
    MissingGeometryFilter selectedFilters() {
        MissingGeometryFilter filter = null;
        if (txtCount.getInputVerifier().verify(txtCount)) {
            Status status = null;
            if (btnGroupStatus.getSelection() != null) {
                status = Status.valueOf(btnGroupStatus.getSelection().getActionCommand());
            }
            final String countStr = txtCount.getText().trim();
            final Integer count = countStr.isEmpty() ? null : Integer.parseInt(countStr);
            final EnumSet<TileType> types = selectedTypes();
            filter = new MissingGeometryFilter(status, types, count);
        }
        return filter;
    }

    private EnumSet<TileType> selectedTypes() {
        final EnumSet<TileType> types = EnumSet.noneOf(TileType.class);
        if (cbTypeRoad.isSelected()) {
            types.add(TileType.ROAD);
        }
        if (cbTypeParking.isSelected()) {
            types.add(TileType.PARKING);
        }
        if (cbTypeBoth.isSelected()) {
            types.add(TileType.BOTH);
        }
        if (cbIncludeWater.isSelected()) {
            types.add(TileType.WATER);
        }
        if (cbIncludePath.isSelected()) {
            types.add(TileType.PATH);
        }
        return types;
    }

    private MissingGeometryGuiConfig getGuiCnf() {
        return MissingGeometryGuiConfig.getInstance();
    }

    private void selectStatus(final Status status) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    rbStatusOpen.setSelected(true);
                    break;
                case SOLVED:
                    rbStatusSolved.setSelected(true);
                    break;
                default:
                    rbStatusInvalid.setSelected(true);
                    break;
            }
        } else {
            btnGroupStatus.clearSelection();
        }
    }

    private void selectTypes(final EnumSet<TileType> types) {
        if (types != null && !types.isEmpty()) {
            boolean selected = types.contains(TileType.ROAD);
            cbTypeRoad.setSelected(selected);
            selected = types.contains(TileType.PARKING);
            cbTypeParking.setSelected(selected);
            selected = types.contains(TileType.BOTH);
            cbTypeBoth.setSelected(selected);
        } else {
            cbTypeRoad.setSelected(false);
            cbTypeParking.setSelected(false);
            cbTypeBoth.setSelected(false);
        }
    }
}