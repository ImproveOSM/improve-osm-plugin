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
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;


/**
 * Defines a custom model for the {@code TurnRestriction} objects.
 *
 * @author Beata
 * @version $Revision$
 */
class TurnRestrictionsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -6609573772691997993L;

    private static final int IDX_TYPE = 0;
    private static final int IDX_CONFIDENCE = 1;
    private static final int IDX_PASSES = 2;
    private static final int IDX_STATUS = 3;
    private static final int IDX_IDENTIFIER = 4;

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
            final TurnRestriction turnRestricion = data.get(rowIndex);
            switch (columnIndex) {
                case IDX_TYPE:
                    value = Formatter.formatTurnType(turnRestricion.getTurnType());
                    break;
                case IDX_CONFIDENCE:
                    value = turnRestricion.getConfidenceLevel();
                    break;
                case IDX_PASSES:
                    value = turnRestricion.getNumberOfPasses();
                    break;
                case IDX_STATUS:
                    value = turnRestricion.getStatus();
                    break;
                case IDX_IDENTIFIER:
                    value = turnRestricion.getId();
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