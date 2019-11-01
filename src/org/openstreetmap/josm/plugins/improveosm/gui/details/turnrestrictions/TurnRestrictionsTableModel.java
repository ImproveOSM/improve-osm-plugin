/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import com.telenav.josm.common.entity.Pair;


/**
 * Defines a custom model for the {@code TurnRestriction} objects.
 *
 * @author Beata
 * @version $Revision$
 */
class TurnRestrictionsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -6609573772691997993L;

    private static final int IDX_TYPE = 0;
    private static final int IDX_FIRST_SEG_TRIPS = 1;
    private static final int IDX_CONFIDENCE = 2;
    private static final int IDX_STATUS = 3;

    private List<TurnRestriction> data;


    TurnRestrictionsTableModel(final List<TurnRestriction> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return TurnRestrictionGuiConfig.getInstance().getTblHeader().size();
    }

    @Override
    public String getColumnName(final int colIdx) {
        return TurnRestrictionGuiConfig.getInstance().getTblHeader().get(colIdx);
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object value = null;
        if (rowIndex > -1 && rowIndex < data.size()) {
            final TurnRestriction turnRestriction = data.get(rowIndex);
            switch (columnIndex) {
                case IDX_TYPE:
                    value = turnRestriction.getTurnType();
                    break;
                case IDX_FIRST_SEG_TRIPS:
                    value = new Pair<>(turnRestriction.getSegments().get(0).getNumberOfTrips(),
                            turnRestriction.getNumberOfPasses());
                    break;
                case IDX_CONFIDENCE:
                    value = turnRestriction.getConfidenceLevel();
                    break;
                case IDX_STATUS:
                    value = turnRestriction.getStatus();
                    break;
                default:
                    value = rowIndex;
                    break;
            }
        }
        return value;
    }

    TurnRestriction getRow(final int rowIndex) {
        return (rowIndex > -1 && rowIndex < data.size()) ? data.get(rowIndex) : null;
    }

    void setData(final List<TurnRestriction> data) {
        this.data = data != null ? new ArrayList<>(data) : null;
    }

    List<TurnRestriction> getData() {
        return data;
    }
}