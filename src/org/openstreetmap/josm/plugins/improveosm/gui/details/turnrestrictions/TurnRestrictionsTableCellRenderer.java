/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import com.telenav.josm.common.entity.Pair;


/**
 * Defines a custom table cell renderer for {@code TurnRestriction} objects.
 *
 * @author Beata
 * @version $Revision$
 */
class TurnRestrictionsTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 9111934633585458749L;


    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(MainApplication.getMap().getFont().deriveFont(Font.PLAIN));
        if (value != null) {
            String txt;
            if (value instanceof Status) {
                txt = ((Status) value).name().toLowerCase();
            } else if (value instanceof TurnConfidenceLevel) {
                txt = value.toString();
            } else if (value instanceof Pair<?, ?>) {
                txt = Formatter.formatFirstLastTripsNumber((Pair<Integer, Integer>) value);
            } else {
                if (column == 0) {
                    setIcon(TurnTypeIconFactory.getInstance().getIcon(value.toString()));
                    txt = Formatter.formatTurnType(value.toString());
                } else {
                    txt = value.toString();
                }
            }
            setText(txt);
        }
        return this;
    }
}