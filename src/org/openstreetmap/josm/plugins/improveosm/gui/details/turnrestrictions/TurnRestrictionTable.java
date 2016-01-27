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

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObservable;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;


/**
 * Custom table displaying a list of {@code TurnRestriction}s.
 *
 * @author Beata
 * @version $Revision$
 */
class TurnRestrictionTable extends JTable implements TurnRestrictionSelectionObservable {

    private static final long serialVersionUID = -2793471615446950298L;
    private static final int COLUMN_SPACE = 8;
    private static final int COLUMNS = 5;
    private static final int TBL_ROW_HEIGHT = 23;
    private TurnRestrictionSelectionObserver observer;


    TurnRestrictionTable() {
        super(new TurnRestrictionsTableModel(null));

        // set table properties
        getTableHeader().setMinimumSize(new Dimension(0, TBL_ROW_HEIGHT));
        getTableHeader().setDefaultRenderer(new TableHeaderCellRenderer());
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setRowHeight(TBL_ROW_HEIGHT);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // set column properties
        for (int i = 0; i < COLUMNS; i++) {
            final TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(new TurnRestrictionsTableCellRenderer());
            column.setResizable(false);
        }
    }


    @Override
    public void addObserver(final TurnRestrictionSelectionObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver(final TurnRestriction item) {
        observer.selectSimpleTurnRestriction(item);
    }

    @Override
    public void valueChanged(final ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            final int selRow = getSelectedRow();
            final TurnRestriction item = selRow > -1 ? ((TurnRestrictionsTableModel) getModel()).getRow(selRow) : null;
            if (item != null) {
                notifyObserver(item);
            }
        }
    }

    /**
     * Updates the turn restriction table with the new list of {@code TurnRestriction}s
     *
     * @param data a list of {@code TurnRestriction}s
     */
    void updateData(final List<TurnRestriction> data) {
        ((TurnRestrictionsTableModel) getModel()).setData(data);
        clearSelection();
        columnResize();
    }

    private void columnResize() {
        for (int column = 0; column < COLUMNS; column++) {
            final TableColumn tableColumn = getColumnModel().getColumn(column);
            int width = 0;
            for (int row = 0; row < getRowCount(); row++) {
                final TableCellRenderer renderer = getCellRenderer(row, column);
                final Component comp = prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width, width + 2 * getIntercellSpacing().width * COLUMN_SPACE);
            }
            tableColumn.setMinWidth(width);
            tableColumn.setPreferredWidth(width);
        }
    }

    /**
     * Checks if the turn restrictions table contains or not the given turn restriction.
     *
     * @param turnRestriction a {@code TurnRestriction} object
     * @return true if the table contains the given element/false otherwise
     */
    boolean contains(final TurnRestriction turnRestriction) {
        final TurnRestrictionsTableModel model = (TurnRestrictionsTableModel) getModel();
        return model.getData() != null && model.getData().contains(turnRestriction);
    }
}