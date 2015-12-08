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
package org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.details.EditDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Utils;


/**
 * Defines the components of the button panel used in the {@code DirectionOfFlowDetailsDialog} window.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel {

    private static final long serialVersionUID = 2035810847510862585L;

    private static final Dimension DIM = new Dimension(200, 23);
    private static final int ROWS = 1;
    private static final int COLS = 6;

    /* UI components */
    private final JButton btnFilter;
    private final JButton btnLocation;
    private final JButton btnComment;
    private final JButton btnSolve;
    private final JButton btnReopen;
    private final JButton btnInvalid;

    /* currently selected road segment */
    private RoadSegment roadSegment;
    private CommentObserver commentObserver;


    /**
     * Builds a new button panel.
     */
    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final DirectionOfFlowGuiConfig guiCnf = DirectionOfFlowGuiConfig.getInstance();
        final IconConfig iconCnf = IconConfig.getInstance();

        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialog(), iconCnf.getFilterIcon(),
                GuiConfig.getInstance().getBtnFilterTlt(),
                true);
        btnLocation = GuiBuilder.buildButton(new CopyLocationAction(), iconCnf.getLocationIcon(),
                guiCnf.getBtnLocationTlt(), true);
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
        add(btnLocation);
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
    void setRoadSegment(final RoadSegment roadSegment) {
        this.roadSegment = roadSegment;
        if (this.roadSegment != null) {
            btnComment.setEnabled(true);
            if (this.roadSegment.getStatus() == Status.OPEN) {
                btnSolve.setEnabled(true);
                btnReopen.setEnabled(false);
                btnInvalid.setEnabled(true);
            } else {
                btnSolve.setEnabled(false);
                btnReopen.setEnabled(true);
                btnInvalid.setEnabled(false);
            }
        } else {
            btnComment.setEnabled(false);
            btnSolve.setEnabled(false);
            btnReopen.setEnabled(false);
            btnInvalid.setEnabled(false);
        }
    }

    /**
     * Registers the comment observer.
     *
     * @param commentObserver a {@code CommentObserver}
     */
    void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
    }


    /* displays the FilterDialog window */

    private class DisplayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 1260997951329896682L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                final FilterDialog dialog = new FilterDialog();
                dialog.setVisible(true);

            }
        }
    }

    /* copies the selected segment's/current position to clipboard */

    private class CopyLocationAction extends AbstractAction {

        private static final long serialVersionUID = 5864772613263351452L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof JButton) {
                LatLon latLon = null;
                if (roadSegment != null) {
                    final int idx = Math.round(roadSegment.getPoints().size() / 2);
                    latLon = roadSegment.getPoints().get(idx);
                } else {
                    latLon = Main.map.mapView.getRealBounds().getCenter();
                }
                Utils.copyToClipboard(Formatter.formatLatLon(latLon));
            }
        }
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