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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;


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
        final Font font = Main.map.getFont().deriveFont(Font.BOLD, GuiBuilder.FONT_SIZE);
        setFont(font);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setText(value.toString());

        final int width = Main.map.getFontMetrics(font).stringWidth(getText()) + table.getIntercellSpacing().width;
        if (table.getColumnModel().getColumn(column).getMinWidth() < width) {
            table.getColumnModel().getColumn(column).setMinWidth(width);
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
        return this;
    }
}