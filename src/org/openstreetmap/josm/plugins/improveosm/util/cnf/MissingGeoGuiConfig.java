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
 *
 * @author Beata
 * @version $Revision$
 */
public final class MissingGeoGuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "missinggeo_gui.properties";
    private static final MissingGeoGuiConfig INSTANCE = new MissingGeoGuiConfig();

    private final String layerName;
    private final String layerTlt;

    /* buttons related text */
    private final String btnFilterTlt;
    private final String btnCommentTlt;
    private final String btnOpenTlt;
    private final String btnSolveTlt;
    private final String btnInvalidTlt;

    /* edit dialog window related texts */
    private final String dlgSolveTitle;
    private final String dlgReopenTitle;
    private final String dlgInvalidTitle;

    /* filter dialog texts */
    private final String dlgFilterLblStatus;
    private final String dlgFilterLblType;
    private final String dlgFilterLblWater;
    private final String dlgFilterLblPedestrian;

    /* details panel related texts */
    private final String pnlTileTitle;
    private final String pnlHistoryTitle;
    private final String lblId;
    private final String lblTimestamp;
    private final String lblStatus;
    private final String lblType;

    /* commonly used texts */
    private final String lblTripCount;
    private final String lblPointCount;
    private final String lblDisplay;


    private MissingGeoGuiConfig() {
        super(CONFIG_FILE);

        layerName = readProperty("layer.name");
        layerTlt = readProperty("layer.tlt");
        btnFilterTlt = readProperty("btn.filter.tlt");
        btnCommentTlt = readProperty("btn.comment.tlt");
        btnOpenTlt = readProperty("btn.open.tlt");
        btnSolveTlt = readProperty("btn.solve.tlt");
        btnInvalidTlt = readProperty("btn.invalid.tlt");
        dlgSolveTitle = readProperty("edit.dialog.solve.title");
        dlgReopenTitle = readProperty("edit.dialog.reopen.title");
        dlgInvalidTitle = readProperty("edit.dialog.invalid.title");
        dlgFilterLblStatus = readProperty("dialog.filter.status.lbl");
        dlgFilterLblType = readProperty("dialog.filter.type.lbl");
        dlgFilterLblWater = readProperty("dialog.filter.water.lbl");
        dlgFilterLblPedestrian = readProperty("dialog.filter.pedestrian.lbl");
        pnlTileTitle = readProperty("details.tile.title");
        pnlHistoryTitle = readProperty("details.history.title");
        lblId = readProperty("details.id.lbl");
        lblTimestamp = readProperty("details.time.lbl");
        lblStatus = readProperty("details.status.lbl");
        lblType = readProperty("details.type.lbl");
        lblTripCount = readProperty("lbl.tripCount");
        lblPointCount = readProperty("lbl.pointCount");
        lblDisplay = readProperty("lbl.display");
    }


    public static MissingGeoGuiConfig getInstance() {
        return INSTANCE;
    }

    public String getLayerName() {
        return layerName;
    }

    public String getLayerTlt() {
        return layerTlt;
    }

    public String getBtnFilterTlt() {
        return btnFilterTlt;
    }

    public String getBtnCommentTlt() {
        return btnCommentTlt;
    }

    public String getBtnOpenTlt() {
        return btnOpenTlt;
    }

    public String getBtnSolveTlt() {
        return btnSolveTlt;
    }

    public String getBtnInvalidTlt() {
        return btnInvalidTlt;
    }

    public String getDlgSolveTitle() {
        return dlgSolveTitle;
    }

    public String getDlgReopenTitle() {
        return dlgReopenTitle;
    }

    public String getDlgInvalidTitle() {
        return dlgInvalidTitle;
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

    public String getPnlTileTitle() {
        return pnlTileTitle;
    }

    public String getPnlHistoryTitle() {
        return pnlHistoryTitle;
    }

    public String getLblId() {
        return lblId;
    }

    public String getLblTimestamp() {
        return lblTimestamp;
    }

    public String getLblStatus() {
        return lblStatus;
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