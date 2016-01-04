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
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicButtonPanel;


/**
 * Defines the components of the button panel used in the {@code MissingGeometryDetailsDialog} window.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends BasicButtonPanel<Tile> {

    private static final long serialVersionUID = 7563325113402557461L;

    private static final int BUTTONS_COUNT = 5;



    ButtonPanel() {
        super(BUTTONS_COUNT, new DislayFilterDialog());
    }


    @Override
    public void enablePanelActions() {
        if (getItem().getStatus() == Status.OPEN) {
            enablePanelActions(true, true, false, true);
        } else {
            enablePanelActions(true, false, true, false);
        }
    }


    /* displays the filter dialog window */
    private static class DislayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 2260459345028599219L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                final FilterDialog dialog = new FilterDialog();
                dialog.setVisible(true);
            }
        }
    }
}