/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.openstreetmap.josm.gui.MainApplication;


/**
 * Custom table header renderer for the {@code TurnRestrictionTable}.
 *
 * @author Beata
 * @version $Revision$
 */
class TableHeaderCellRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 9111934633585458749L;


    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        // set foreground color
        if (table.isCellSelected(row, column)) {
            setForeground(Color.blue);
        } else if (table.isRowSelected(row)) {
            setForeground(Color.red);
        } else {
            setForeground(Color.black);
        }
        final Font font = MainApplication.getMap().getFont().deriveFont(Font.BOLD);
        setFont(font);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setText(value.toString());

        final int width = MainApplication.getMap().getFontMetrics(font).stringWidth(getText())
                + table.getIntercellSpacing().width;
        if (table.getColumnModel().getColumn(column).getMinWidth() < width) {
            table.getColumnModel().getColumn(column).setMinWidth(width);
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
        return this;
    }
}