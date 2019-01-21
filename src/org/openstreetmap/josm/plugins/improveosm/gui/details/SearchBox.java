/*
 *  Copyright 2018 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.widgets.DisableShortcutsOnFocusGainedTextField;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;


/**
 * Builds a text field for searching a location.
 *
 * @author nicoletav
 */
class SearchBox extends DisableShortcutsOnFocusGainedTextField implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final int ELEMENTS_NR = 2;
    private static final int LAT_INDEX = 0;
    private static final int LON_INDEX = 1;
    private static final double MAX_LAT_VALUE = 85.05;
    private static final double MIN_LAT_VALUE = -85.05;

    SearchBox() {
        super(GuiConfig.getInstance().getInitialTxt());
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String latLonValue = this.getText();
        if (latLonValue.split(",").length == ELEMENTS_NR) {
            try {
                final double lat = Double.parseDouble(latLonValue.split(",")[LAT_INDEX]);
                final double lon = Double.parseDouble(latLonValue.split(",")[LON_INDEX]);
                LatLon searchedLocation = new LatLon(lat, lon);
                if (searchedLocation.isValid()) {
                    if (!isLocationSearchable(searchedLocation.getY())) {
                        searchedLocation = getExtremityPoint(searchedLocation);
                    }
                    final EastNorth demoZoomLocation =
                            searchedLocation.getEastNorth(MainApplication.getMap().mapView.getProjection());
                    MainApplication.getMap().mapView.zoomTo(demoZoomLocation, 1);
                } else {
                    this.setText(GuiConfig.getInstance().getIncorrectValuesTxt());
                }

            } catch (final NumberFormatException e1) {
                this.setText(GuiConfig.getInstance().getIncorrectFormatTxt());
            }
        } else {
            this.setText(GuiConfig.getInstance().getIncorrectElementsNr());
        }
        MainApplication.getMap().mapView.requestFocus();
    }

    /*
        Checks if the latitude value can be displayed.

        Display of the geographical point is possible for a latitude value within [-85.05, 85,05] due to the map space.
     */
    private boolean isLocationSearchable(final double latitude) {
        boolean isSearchable = false;
        if (MIN_LAT_VALUE <= latitude && latitude <= MAX_LAT_VALUE) {
            isSearchable = true;
        }
        return isSearchable;
    }

    private LatLon getExtremityPoint(final LatLon point) {
        LatLon extremityPoint = null;
        if (point.getY() > MAX_LAT_VALUE) {
            extremityPoint = new LatLon(MAX_LAT_VALUE, point.getX());
        }

        if (point.getY() < MIN_LAT_VALUE) {
            extremityPoint = new LatLon(MIN_LAT_VALUE, point.getX());
        }
        return extremityPoint;
    }
}