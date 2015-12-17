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
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;


/**
 * Defines a custom table cell renderer for {@code TurnRestriction} objects.
 *
 * @author Beata
 * @version $Revision$
 */
class TurnRestrictionsTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 9111934633585458749L;

    TurnRestrictionsTableCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(Main.map.getFont().deriveFont(Font.PLAIN, GuiBuilder.FONT_SIZE));
        if (value != null) {
            String txt = "";
            if (value instanceof Status) {
                txt = ((Status) value).name().toLowerCase();
            } else if (value instanceof TurnConfidenceLevel) {
                txt = ((TurnConfidenceLevel) value).shortDisplayName();
            } else {
                txt = value.toString();
            }
            setText(txt);

        }
        return this;
    }
}