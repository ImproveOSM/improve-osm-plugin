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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.EditDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;


/**
 * Defines the components of the button panel used in the {@code MissingGeometryDetailsDialog} window.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel {

    private static final long serialVersionUID = 7563325113402557461L;

    private static final Dimension DIM = new Dimension(200, 23);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* UI components */
    private final JButton btnFilter;
    private final JButton btnComment;
    private final JButton btnSolve;
    private final JButton btnReOpen;
    private final JButton btnInvalid;

    private CommentObserver commentObserver;
    private Tile tile;


    /**
     * Builds a new object.
     */
    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final MissingGeometryGuiConfig guiCnf = MissingGeometryGuiConfig.getInstance();
        final IconConfig iconCnf = IconConfig.getInstance();
        btnFilter = GuiBuilder.buildButton(new DislayFilterDialog(), iconCnf.getFilterIcon(),
                GuiConfig.getInstance().getBtnFilterTlt(), true);
        final DisplayEditDialog commentAction =
                new DisplayEditDialog(null, GuiConfig.getInstance().getDlgCommentTitle(), iconCnf.getCommentIcon());
        btnComment = GuiBuilder.buildButton(commentAction, iconCnf.getCommentIcon(), guiCnf.getBtnCommentTlt(), false);
        final DisplayEditDialog solveAction = new DisplayEditDialog(Status.SOLVED,
                MissingGeometryGuiConfig.getInstance().getDlgSolveTitle(), iconCnf.getSolvedIcon());
        btnSolve = GuiBuilder.buildButton(solveAction, iconCnf.getSolvedIcon(), guiCnf.getBtnSolveTlt(), false);
        final DisplayEditDialog reopenAction = new DisplayEditDialog(Status.OPEN,
                MissingGeometryGuiConfig.getInstance().getDlgReopenTitle(), iconCnf.getOpenIcon());
        btnReOpen = GuiBuilder.buildButton(reopenAction, iconCnf.getOpenIcon(), guiCnf.getBtnOpenTlt(), false);
        final DisplayEditDialog invalidAction =
                new DisplayEditDialog(Status.INVALID, guiCnf.getDlgInvalidTitle(), iconCnf.getInvalidIcon());
        btnInvalid = GuiBuilder.buildButton(invalidAction, iconCnf.getInvalidIcon(), guiCnf.getBtnInvalidTlt(), false);

        add(btnFilter);
        add(btnComment);
        add(btnSolve);
        add(btnReOpen);
        add(btnInvalid);
        setPreferredSize(DIM);
    }

    /**
     * Registers the comment observer.
     *
     * @param commentObserver a {@code CommentObserver}
     */
    void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
    }

    /**
     * Sets the button's state based on the selected tile. If tile is null then only the filter button remains enabled.
     *
     * @param tile a {@code Tile} represents the currently selected tile
     */
    void setTile(final Tile tile) {
        this.tile = tile;

        if (this.tile != null) {
            btnComment.setEnabled(true);
            if (this.tile.getStatus() == Status.OPEN) {
                btnReOpen.setEnabled(false);
                btnSolve.setEnabled(true);
                btnInvalid.setEnabled(true);
            } else {
                btnReOpen.setEnabled(true);
                btnSolve.setEnabled(false);
                btnInvalid.setEnabled(false);
            }
        } else {
            btnComment.setEnabled(false);
            btnReOpen.setEnabled(false);
            btnSolve.setEnabled(false);
            btnInvalid.setEnabled(false);
        }
    }


    /* displays the filter dialog window */
    private class DislayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 2260459345028599219L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                final FilterDialog dialog = new FilterDialog();
                dialog.setVisible(true);
            }
        }
    }

    /* displays the dialog window for the following action: comment, solve, reopen and invalidate tile(s) */
    private final class DisplayEditDialog extends AbstractAction {

        private static final long serialVersionUID = -4385842842381896038L;
        private final Status status;
        private final String title;
        private final ImageIcon icon;

        private DisplayEditDialog(final Status status, final String title, final ImageIcon icon) {
            this.status = status;
            this.title = title;
            this.icon = icon;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                final EditDialog dialog = new EditDialog(status, title, icon.getImage());
                dialog.registerObserver(commentObserver);
                dialog.setVisible(true);
            }
        }
    }
}