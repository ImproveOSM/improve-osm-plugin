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
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class PopupMenuDisplayAdapter extends MouseAdapter {

    private final JPopupMenu menu;
    private final JComponent component;

    /**
     * Builds a new {@code PopupMenuDisplayAdapter} with the given arguments.
     *
     * @param component a {@code JComponent} object
     * @param menu a {@code JPopupMenu} object
     */
    public PopupMenuDisplayAdapter(final JComponent component, final JPopupMenu menu) {
        this.component = component;
        this.menu = menu;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        showMenu(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        showMenu(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        showMenu(e);
    }

    private void showMenu(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            if (component instanceof JTextComponent) {
                // display menu on text field
                final JTextComponent txtComp = (JTextComponent) component;
                if (txtComp.getSelectedText() != null) {
                    menu.show(component, event.getX(), event.getY());
                }
            } else if (component instanceof JTable) {
                // display menu on table cell
                final JTable tblComp = (JTable) component;
                final int row = tblComp.rowAtPoint(event.getPoint());
                final int col = tblComp.columnAtPoint(event.getPoint());
                if (tblComp.isCellSelected(row, col)) {
                    tblComp.changeSelection(row, col, false, false);
                    menu.show(component, event.getX(), event.getY());
                }
            }
        }
    }
}