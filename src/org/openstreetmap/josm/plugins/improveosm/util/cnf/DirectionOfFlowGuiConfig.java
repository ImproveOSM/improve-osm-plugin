/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;

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

    /* info panel texts */
    private final String lblTrips;
    private final String lblType;
    private final String lblProcent;

    /* commonly used labels */
    private final String lblC1;
    private final String lblC2;
    private final String lblC3;

    /* tip info texts */
    private final String dlgTipTitle;
    private final String dlgTipTxt;


    private DirectionOfFlowGuiConfig() {
        super(CONFIG_FILE);

        layerName = readProperty("plugin.name");
        layerTxt = readProperty("plugin.txt");
        layerTlt = readProperty("plugin.tlt");
        btnLocationTlt = readProperty("btn.location.tlt");
        lblTrips = readProperty("details.info.trips.lbl");
        lblType = readProperty("details.info.type.lbl");
        lblProcent = readProperty("details.info.procent.lbl");
        lblC1 = readProperty("c1.lbl");
        lblC2 = readProperty("c2.lbl");
        lblC3 = readProperty("c3.lbl");
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

    public String getDlgTipTitle() {
        return dlgTipTitle;
    }

    public String getDlgTipTxt() {
        return dlgTipTxt;
    }
}