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
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.DirectionOfFlowFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;


/**
 * Defines the {@code ImproveOsmDetailsDialog} action panel. The user can perform the following actions: filter the
 * data, comment/solve/reopen/invalidate the selected item(s).
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel {

    private static final long serialVersionUID = -1004058684285756450L;

    private static final int ROWS = 1;
    private static final int COLS = 5;
    private static final Dimension DIM = new Dimension(200, 23);

    /* panel components */
    private final JButton btnFilter;
    private final JButton btnComment;
    private final JButton btnSolve;
    private final JButton btnReopen;
    private final JButton btnInvalid;

    private CommentObserver commentObserver;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialog(), iconConfig.getFilterIcon(),
                guiConfig.getBtnFilterTlt(), true);
        btnComment = GuiBuilder.buildButton(
                new DisplayEditDialog(null, guiConfig.getDlgCommentTitle(), iconConfig.getCommentIcon()),
                iconConfig.getCommentIcon(), guiConfig.getBtnCommentTlt(), false);
        btnSolve = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.SOLVED, guiConfig.getDlgSolveTitle(), iconConfig.getSolveIcon()),
                iconConfig.getSolveIcon(), guiConfig.getBtnSolveTlt(), false);
        btnReopen = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.OPEN, guiConfig.getDlgReopenTitle(), iconConfig.getReopenIcon()),
                iconConfig.getReopenIcon(), guiConfig.getBtnReopenTlt(), false);
        btnInvalid = GuiBuilder.buildButton(
                new DisplayEditDialog(Status.INVALID, guiConfig.getDlgInvalidTitle(), iconConfig.getInvalidIcon()),
                iconConfig.getInvalidIcon(), guiConfig.getBtnInvalidTlt(), false);

        add(btnFilter);
        add(btnComment);
        add(btnSolve);
        add(btnReopen);
        add(btnInvalid);
        setPreferredSize(DIM);
    }


    void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
    }

    /**
     * Enables the panel actions based on the given item. By default only the filter action is enabled; the other
     * actions are enabled/disabled based on the selected item status.
     *
     * @param item the currently selected object
     */
    <T> void enablePanelActions(final T item) {
        if (item == null) {
            enablePanelActions(false, false, false, false);
        } else {
            Status status = null;
            if (item instanceof Tile) {
                status = ((Tile) item).getStatus();
            } else if (item instanceof RoadSegment) {
                status = ((RoadSegment) item).getStatus();
            } else if (item instanceof TurnRestriction) {
                status = ((TurnRestriction) item).getStatus();
            }
            if (status != null) {
                if (status == Status.OPEN) {
                    enablePanelActions(true, true, false, true);
                } else {
                    enablePanelActions(true, false, true, false);
                }
            } else {
                enablePanelActions(false, false, false, false);
            }
        }
    }

    private void enablePanelActions(final boolean commmentFlag, final boolean solveFlag, final boolean reopenFlag,
            final boolean invalidFlag) {
        btnComment.setEnabled(commmentFlag);
        btnSolve.setEnabled(solveFlag);
        btnReopen.setEnabled(reopenFlag);
        btnInvalid.setEnabled(invalidFlag);
    }


    /*
     * Displays the filters corresponding to the active data layer.
     */
    private final class DisplayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 3707557295874437209L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (Main.map != null) {
                final Layer activeLayer = Main.map.mapView.getActiveLayer();
                if (activeLayer instanceof MissingGeometryLayer) {
                    new MissingGeometryFilterDialog().setVisible(true);
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    new DirectionOfFlowFilterDialog().setVisible(true);
                } else if (activeLayer instanceof TurnRestrictionLayer) {
                    new TurnRestrictionFilterDialog().setVisible(true);
                }
            }
        }
    }

    /*
     * Displays an edit dialog window for the following user actions: comment, solve, reopen, invalidate.
     */
    private final class DisplayEditDialog extends AbstractAction {

        private static final long serialVersionUID = 3577509903506411276L;
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
            final EditDialog dialog = new EditDialog(status, title, icon.getImage());
            dialog.registerObserver(commentObserver);
            dialog.setVisible(true);
        }
    }
}