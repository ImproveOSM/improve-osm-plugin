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


/**
 * Holds direction of flow logic related texts.
 *
 * @author Beata
 * @version $Revision$
 */
public final class DirectionOfFlowGuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "improveosm_dof_gui.properties";
    private static final DirectionOfFlowGuiConfig INSTANCE = new DirectionOfFlowGuiConfig();

    private final String layerName;
    private final String layerTxt;
    private final String layerTlt;

    /* button tooltips */
    private final String btnLocationTlt;

    /* filter dialog texts */
    private final String dlgFilterStatusLbl;
    private final String dlgFilterConfidenceLbl;

    /* info panel texts */
    private final String lblTrips;
    private final String lblType;
    private final String lblProcent;

    /* commonly used labels */
    private final String lblC1;
    private final String lblC2;
    private final String lblC3;
    private final String lblOneway;

    /* tip info texts */
    private final String dlgTipTitle;
    private final String dlgTipTxt;


    DirectionOfFlowGuiConfig() {
        super(CONFIG_FILE);

        layerName = readProperty("plugin.name");
        layerTxt = readProperty("plugin.txt");
        layerTlt = readProperty("plugin.tlt");
        btnLocationTlt = readProperty("btn.location.tlt");
        dlgFilterStatusLbl = readProperty("dialog.filter.status.lbl");
        dlgFilterConfidenceLbl = readProperty("dialog.filter.confidence.lbl");
        lblTrips = readProperty("details.info.trips.lbl");
        lblType = readProperty("details.info.type.lbl");
        lblProcent = readProperty("details.info.procent.lbl");
        lblC1 = readProperty("c1.lbl");
        lblC2 = readProperty("c2.lbl");
        lblC3 = readProperty("c3.lbl");
        lblOneway = readProperty("oneway.lbl");
        dlgTipTitle = readProperty("dialog.tip.title");
        dlgTipTxt = readProperty("dialog.tip.text");
    }


    public static DirectionOfFlowGuiConfig getInstance() {
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

    public String getBtnLocationTlt() {
        return btnLocationTlt;
    }

    public String getDlgFilterStatusLbl() {
        return dlgFilterStatusLbl;
    }

    public String getDlgFilterConfidenceLbl() {
        return dlgFilterConfidenceLbl;
    }

    public String getLblTrips() {
        return lblTrips;
    }

    public String getLblType() {
        return lblType;
    }

    public String getLblProcent() {
        return lblProcent;
    }

    public String getLblC1() {
        return lblC1;
    }

    public String getLblC2() {
        return lblC2;
    }

    public String getLblC3() {
        return lblC3;
    }

    public String getLblOneway() {
        return lblOneway;
    }

    public String getDlgTipTitle() {
        return dlgTipTitle;
    }

    public String getDlgTipTxt() {
        return dlgTipTxt;
    }
}