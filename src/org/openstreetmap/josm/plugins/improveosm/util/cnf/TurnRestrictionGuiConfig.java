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
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import java.util.List;

/**
 * Holds turn restriction of related texts.
 *
 * @author Beata
 * @version $Revision$
 */
public final class TurnRestrictionGuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "improveosm_tr_gui.properties";
    private static final TurnRestrictionGuiConfig INSTANCE = new TurnRestrictionGuiConfig();

    private final String layerName;
    private final String layerTxt;
    private final String layerTlt;

    /* filter dialog texts */
    private final String dlgFilterStatusLbl;
    private final String dlgFilterConfidenceLbl;

    /* info panel texts */
    private final String lblId;
    private final String lblType;
    private final String lblTrips;
    private final String tblTitle;
    private final List<String> tblHeader;

    /* commonly used labels */
    private final String lblC1;
    private final String lblC2;
    private final String lblTurn;


    private TurnRestrictionGuiConfig() {
        super(CONFIG_FILE);

        /* layer texts */
        layerName = readProperty("plugin.name");
        layerTxt = readProperty("plugin.txt");
        layerTlt = readProperty("plugin.tlt");
        dlgFilterStatusLbl = readProperty("dialog.filter.status.lbl");
        dlgFilterConfidenceLbl = readProperty("dialog.filter.confidence.lbl");

        lblId = readProperty("details.info.id.lbl");
        lblType = readProperty("details.info.type.lbl");
        lblTrips = readProperty("details.info.trips.lbl");
        tblTitle = readProperty("details.info.tbl.title");
        tblHeader = readProperties("details.info.tbl.header");
        lblC1 = readProperty("c1.lbl");
        lblC2 = readProperty("c2.lbl");
        lblTurn = readProperty("turn.lbl");

    }

    public static TurnRestrictionGuiConfig getInstance() {
        return INSTANCE;
    }

    public String getLayerName() {
        return layerName;
    }

    public String getLayerTxt() {
        return layerTxt;
    }

    public String getLayerTlt() {
        return layerTlt;
    }

    public String getDlgFilterStatusLbl() {
        return dlgFilterStatusLbl;
    }

    public String getDlgFilterConfidenceLbl() {
        return dlgFilterConfidenceLbl;
    }

    public String getLblId() {
        return lblId;
    }

    public String getLblType() {
        return lblType;
    }

    public String getLblTrips() {
        return lblTrips;
    }

    public String getTblTitle() {
        return tblTitle;
    }

    public List<String> getTblHeader() {
        return tblHeader;
    }

    public String getLblC1() {
        return lblC1;
    }

    public String getLblC2() {
        return lblC2;
    }

    public String getLblTurn() {
        return lblTurn;
    }
}