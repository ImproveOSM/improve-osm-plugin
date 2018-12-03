package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.widgets.DisableShortcutsOnFocusGainedTextField;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;

class SearchBox extends DisableShortcutsOnFocusGainedTextField implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    
    private static final int ELEMENTS_NR = 2;
    private static final int LAT_INDEX = 0;
    private static final int LON_INDEX = 1;

    SearchBox(){
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
                final LatLon searchedLocation = new LatLon(lat, lon);
                if (searchedLocation.isValid()) {
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
}