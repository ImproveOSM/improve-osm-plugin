/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import java.util.List;
import com.telenav.josm.common.cnf.BaseConfig;


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

    /* info panel texts */
    private final String lblType;
    private final String lblFirstSegmentTrips;
    private final String lblLastSegmentTrips;
    private final String tblTitle;
    private final List<String> tblHeader;

    /* commonly used labels */
    private final String lblC1;
    private final String lblC2;
    

    private TurnRestrictionGuiConfig() {
        super(CONFIG_FILE);

        /* layer texts */
        layerName = readProperty("plugin.name");
        layerTxt = readProperty("plugin.txt");
        layerTlt = readProperty("plugin.tlt");

        lblType = readProperty("details.info.type.lbl");
        lblFirstSegmentTrips = readProperty("details.info.segment.first.lbl");
        lblLastSegmentTrips = readProperty("details.info.segment.last.lbl");
        tblTitle = readProperty("details.info.tbl.title");
        tblHeader = readPropertiesList("details.info.tbl.header");
        lblC1 = readProperty("c1.lbl");
        lblC2 = readProperty("c2.lbl");
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

    public String getLblType() {
        return lblType;
    }

    public String getLblFirstSegmentTrips() {
        return lblFirstSegmentTrips;
    }

    public String getLblLastSegmentTrips() {
        return lblLastSegmentTrips;
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
}