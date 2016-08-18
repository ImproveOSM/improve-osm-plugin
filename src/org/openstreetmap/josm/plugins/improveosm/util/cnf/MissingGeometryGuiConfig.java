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