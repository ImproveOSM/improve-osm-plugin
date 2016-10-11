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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.CoordinateFormat;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.datatransfer.ClipboardUtils;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Site;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.comment.DisplayEditDialogAction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.comment.EditPopupMenu;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.DirectionOfFlowFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.GuiBuilder;


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
    private final JButton btnLocation;

    private CommentObserver commentObserver;
    private LatLon selectedItemCoordinate;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialog(), iconConfig.getFilterIcon(),
                guiConfig.getBtnFilterTlt(), true);
        btnComment = GuiBuilder.buildButton(
                new DisplayEditDialogAction(null, guiConfig.getDlgCommentTitle(), iconConfig.getCommentIcon()),
                iconConfig.getCommentIcon(), guiConfig.getBtnCommentTlt(), false);
        btnSolve = GuiBuilder.buildButton(
                new DisplayEditPopupMenu(Status.SOLVED, guiConfig.getDlgSolveTitle(),
                        guiConfig.getMenuSolveCommentTitle(), iconConfig.getSolveIcon()),
                iconConfig.getSolveIcon(), guiConfig.getBtnSolveTlt(), false);
        btnReopen = GuiBuilder.buildButton(
                new DisplayEditPopupMenu(Status.OPEN, guiConfig.getDlgReopenTitle(),
                        guiConfig.getMenuReopenCommentTitle(), iconConfig.getReopenIcon()),
                iconConfig.getReopenIcon(), guiConfig.getBtnReopenTlt(), false);
        btnInvalid = GuiBuilder.buildButton(
                new DisplayEditPopupMenu(Status.INVALID, guiConfig.getDlgInvalidTitle(),
                        guiConfig.getMenuInvalidCommentTitle(), iconConfig.getInvalidIcon()),
                iconConfig.getInvalidIcon(), guiConfig.getBtnInvalidTlt(), false);
        btnLocation = GuiBuilder.buildButton(new HandleLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), true);
        add(btnFilter);
        add(btnComment);
        add(btnSolve);
        add(btnReopen);
        add(btnInvalid);
        add(btnLocation);
        setPreferredSize(DIM);
    }

    void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
        ((DisplayEditDialogAction) btnComment.getAction()).registerCommentObserver(commentObserver);
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
            selectedItemCoordinate = null;
        } else {
            Status status = null;
            if (item instanceof Tile) {
                final Tile tile = (Tile) item;
                status = tile.getStatus();
                selectedItemCoordinate = tile.getPoints().get(0);
            } else if (item instanceof RoadSegment) {
                final RoadSegment roadSegment = (RoadSegment) item;
                status = roadSegment.getStatus();
                selectedItemCoordinate = roadSegment.getPoints().get(0);
            } else if (item instanceof TurnRestriction) {
                final TurnRestriction turnRestriction = (TurnRestriction) item;
                status = turnRestriction.getStatus();
                selectedItemCoordinate = turnRestriction.getPoint() != null ? turnRestriction.getPoint()
                        : (turnRestriction.getTurnRestrictions() != null
                        ? turnRestriction.getTurnRestrictions().get(0).getPoint() : null);
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
                final Layer activeLayer = Util.getImproveOsmLayer();
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
     * Displays a pop-up menu for the following actions: solve item, re-open item and invalidate item.
     */
    private final class DisplayEditPopupMenu extends AbstractAction {

        private static final long serialVersionUID = 8129418961741501231L;
        private static final int POZ_Y = 4;
        private final Status status;
        private final String titleEdit;
        private final String titleEditComment;
        private final ImageIcon icon;


        private DisplayEditPopupMenu(final Status status, final String titleEdit, final String titleEditComment,
                final ImageIcon icon) {
            this.status = status;
            this.titleEdit = titleEdit;
            this.titleEditComment = titleEditComment;
            this.icon = icon;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final EditPopupMenu menu = new EditPopupMenu(status, titleEdit, titleEditComment, icon);
            menu.registerCommentObserver(commentObserver);
            final JButton cmpParent = (JButton) event.getSource();
            final Point point = cmpParent.getMousePosition();
            int x = 0;
            int y = 0;
            if (cmpParent.getMousePosition() != null) {
                x = point.x;
                y = point.y - cmpParent.getWidth() / POZ_Y;
            }
            menu.show(cmpParent, x, y);
        }
    }

    /*
     * Handle the corresponding location button behavior.
     */
    private final class HandleLocationAction extends AbstractAction {

        private static final long serialVersionUID = -7481740371748226905L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final LatLon location = selectedItemCoordinate != null ? selectedItemCoordinate
                    : Main.map.mapView.getRealBounds().getCenter();
            final int zoom = org.openstreetmap.josm.plugins.improveosm.util.Util.zoom(Main.map.mapView.getRealBounds());

            switch (PreferenceManager.getInstance().loadLocationPrefOption()) {
                case OPEN_STREET_VIEW:
                    openURL(Site.OPEN_STREET_VIEW, location, zoom);
                    break;
                case CUSTOM_SITE:
                    handleCustomSite(location, zoom);
                    break;
                case COPY_LOCATION:
                    ClipboardUtils.copyString(Formatter.formatLatLon(location));
            }
        }

        private void handleCustomSite(final LatLon position, final int zoom) {
            final String savedUrl = PreferenceManager.getInstance().loadLocationPrefValue();
            final Site url = Site.getSiteByPrefix(savedUrl);
            if (url != null) {
                openURL(url, position, zoom);
                return;
            }
        }

        private void openURL(final Site emptyUrl, final LatLon position, final int zoom) {
            final String url = emptyUrl.createURL(position.latToString(CoordinateFormat.getDefaultFormat()),
                    position.lonToString(CoordinateFormat.getDefaultFormat()), String.valueOf(zoom));
            try {
                OpenBrowser.displayUrl(new URI(url));
            } catch (final URISyntaxException e) {
                // should not arrive here
            }
        }
    }
}