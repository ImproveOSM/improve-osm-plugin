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
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 * Displays the possible data filters: status, tile types, number of points/number of trips. For small zoom levels the
 * number of points filter is taken into considerations, for big zoom level the number of trips filter is taken into
 * consideration.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterPanel extends BasicFilterPanel {

    private static final long serialVersionUID = -2111695621098772652L;

    /* type filter backgrounds */
    private static final Color PARKING = new Color(236, 232, 131);
    private static final Color BOTH = new Color(243, 203, 131);
    private static final Color WATER = new Color(0, 154, 205);
    private static final Color PATH = new Color(205, 172, 151);
    private static final Color ROAD = new Color(213, 172, 215);


    /* UI components */
    private JCheckBox cbTypeParking;
    private JCheckBox cbTypeRoad;
    private JCheckBox cbTypeBoth;
    private JCheckBox cbIncludeWater;
    private JCheckBox cbIncludePath;
    private JTextField txtCount;


    FilterPanel(final MissingGeometryFilter filter) {
        super(filter, MissingGeometryGuiConfig.getInstance().getDlgFilterLblStatus());

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


    private void addTypesFilter(final Set<TileType> types) {
        add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getDlgFilterLblType(), Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_TYPE);
        cbTypeParking = CheckBoxBuilder.build(TileType.PARKING.displayValue(), Font.PLAIN, PARKING, false);
        cbTypeRoad = CheckBoxBuilder.build(TileType.ROAD.displayValue(), Font.PLAIN, ROAD, false);
        cbTypeBoth = CheckBoxBuilder.build(TileType.BOTH.displayValue(), Font.PLAIN, BOTH, false);
        selectTypes(types);
        add(cbTypeParking, Constraints.RB_PARKING);
        add(cbTypeRoad, Constraints.RB_ROAD);
        add(cbTypeBoth, Constraints.RB_BOTH);
    }

    private void addIncludeWaterFilter(final boolean includeWater) {
        add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getDlgFilterLblWater(), Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_WATER);
        cbIncludeWater = CheckBoxBuilder.build(MissingGeometryGuiConfig.getInstance().getLblDisplay(), Font.PLAIN,
                WATER, includeWater);
        add(cbIncludeWater, Constraints.RB_WATER);
    }

    private void addIncludePathFilter(final boolean includePath) {
        add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getDlgFilterLblPedestrian(), Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PEDESTRIAN);
        cbIncludePath = CheckBoxBuilder.build(MissingGeometryGuiConfig.getInstance().getLblDisplay(), Font.PLAIN, PATH,
                includePath);
        cbIncludePath.setSelected(includePath);
        add(cbIncludePath, Constraints.RB_PATH);
    }

    private void addCountFilter(final Integer value) {
        final String lblTxt =
                Util.zoom(MainApplication.getMap().mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()
                ? MissingGeometryGuiConfig.getInstance().getLblTripCount()
                : MissingGeometryGuiConfig.getInstance().getLblPointCount();
        add(LabelBuilder.build(lblTxt, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP), Constraints.LBL_COUNT);
        txtCount = TextComponentBuilder.buildIntegerTextField(value, 0, null, Font.PLAIN, Color.WHITE, true);
        add(txtCount, Constraints.TXT_COUNT);
    }

    /**
     * Resets the filters to it's initial state.
     */
    @Override
    public void resetFilters() {
        super.resetFilters();
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
    @Override
    public MissingGeometryFilter selectedFilters() {
        final Status status = super.selectedFilters().getStatus();
        final String countStr = txtCount.getText().trim();
        final Integer count = countStr.isEmpty() ? null : Integer.parseInt(countStr);
        final EnumSet<TileType> types = selectedTypes();
        return new MissingGeometryFilter(status, types, count);
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

    private void selectTypes(final Set<TileType> types) {
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