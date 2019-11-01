/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Holds missing geometry logic related texts.
 *
 * @author Beata
 * @version $Revision$
 */
public final class MissingGeometryGuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "improveosm_mg_gui.properties";
    private static final MissingGeometryGuiConfig INSTANCE = new MissingGeometryGuiConfig();

    private final String layerName;
    private final String layerTxt;
    private final String layerTlt;

    /* filter dialog texts */
    private final String dlgFilterLblStatus;
    private final String dlgFilterLblType;
    private final String dlgFilterLblWater;
    private final String dlgFilterLblPedestrian;

    /* details panel related texts */
    private final String lblTimestamp;
    private final String lblType;

    /* commonly used texts */
    private final String lblTripCount;
    private final String lblPointCount;
    private final String lblDisplay;


    private MissingGeometryGuiConfig() {
        super(CONFIG_FILE);

        layerName = readProperty("layer.name");
        layerTxt = readProperty("layer.txt");
        layerTlt = readProperty("layer.tlt");
        dlgFilterLblStatus = readProperty("dialog.filter.status.lbl");
        dlgFilterLblType = readProperty("dialog.filter.type.lbl");
        dlgFilterLblWater = readProperty("dialog.filter.water.lbl");
        dlgFilterLblPedestrian = readProperty("dialog.filter.pedestrian.lbl");
        lblTimestamp = readProperty("details.time.lbl");
        lblType = readProperty("details.type.lbl");
        lblTripCount = readProperty("lbl.tripCount");
        lblPointCount = readProperty("lbl.pointCount");
        lblDisplay = readProperty("lbl.display");
    }


    public static MissingGeometryGuiConfig getInstance() {
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

    public String getDlgFilterLblStatus() {
        return dlgFilterLblStatus;
    }

    public String getDlgFilterLblType() {
        return dlgFilterLblType;
    }

    public String getDlgFilterLblWater() {
        return dlgFilterLblWater;
    }

    public String getDlgFilterLblPedestrian() {
        return dlgFilterLblPedestrian;
    }

    public String getLblTimestamp() {
        return lblTimestamp;
    }

    public String getLblType() {
        return lblType;
    }

    public String getLblTripCount() {
        return lblTripCount;
    }

    public String getLblPointCount() {
        return lblPointCount;
    }

    public String getLblDisplay() {
        return lblDisplay;
    }
}