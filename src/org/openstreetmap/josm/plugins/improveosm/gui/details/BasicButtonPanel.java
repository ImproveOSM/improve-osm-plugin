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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicButtonPanel<T> extends JPanel {

    private static final long serialVersionUID = -5740658979188410760L;

    private static final Dimension DIM = new Dimension(200, 23);

    /* UI components */
    private final JButton btnFilter;
    private final JButton btnComment;
    private final JButton btnSolve;
    private final JButton btnReopen;
    private final JButton btnInvalid;

    /* currently selected item */
    private T item;
    private CommentObserver commentObserver;

    /**
     * Builds a new button panel.
     */
    public BasicButtonPanel(final int rows, final int cols, final AbstractAction filterAction) {
        super(new GridLayout(rows, cols));
        final GuiConfig guiCnf = GuiConfig.getInstance();
        final IconConfig iconCnf = IconConfig.getInstance();

        btnFilter = GuiBuilder.buildButton(filterAction, iconCnf.getFilterIcon(),
                GuiConfig.getInstance().getBtnFilterTlt(), true);
        btnComment = GuiBuilder.buildButton(
                new DisplayEditDialog(null, GuiConfig.getInstance().getDlgCommentTitle(), iconCnf.getCommentIcon()),
                iconCnf.getCommentIcon(), guiCnf.getBtnCommentTlt(), false);
        btnSolve = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.SOLVED, guiCnf.getDlgSolveTitle(), iconCnf.getSolvedIcon()),
                iconCnf.getSolvedIcon(), guiCnf.getBtnSolveTlt(), false);
        btnReopen = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.OPEN, guiCnf.getDlgReopenTitle(), iconCnf.getOpenIcon()),
                iconCnf.getOpenIcon(), guiCnf.getBtnReopenTlt(), false);
        btnInvalid = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.INVALID, guiCnf.getDlgInvalidTitle(), iconCnf.getInvalidIcon()),
                iconCnf.getInvalidIcon(), guiCnf.getBtnInvalidTlt(), false);

        add(btnFilter);
        add(btnComment);
        add(btnSolve);
        add(btnReopen);
        add(btnInvalid);
        setPreferredSize(DIM);
    }

    /**
     * Sets the currently selected road segment. The button panel button's state is modified based on the selected
     * segment status.
     *
     * @param roadSegment a {@code RoadSegment}
     */
    public void setItem(final T item) {
        this.item = item;
        if (this.item != null) {
            enablePanelActions();
        } else {
            enablePanelActions(false, false, false, false);
        }
    }

    public T getItem() {
        return item;
    }

    public abstract void enablePanelActions();

    public void enablePanelActions(final boolean commentFlag, final boolean solveFlag, final boolean reopenFlag,
            final boolean invalidFlag) {
        btnComment.setEnabled(commentFlag);
        btnSolve.setEnabled(solveFlag);
        btnReopen.setEnabled(reopenFlag);
        btnInvalid.setEnabled(invalidFlag);
    }

    public void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
    }


    /* displays the dialog window for the following actions: comment, solve, reopen and invalidate road segment(s) */

    private final class DisplayEditDialog extends AbstractAction {

        private static final long serialVersionUID = 5626725687385429883L;
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