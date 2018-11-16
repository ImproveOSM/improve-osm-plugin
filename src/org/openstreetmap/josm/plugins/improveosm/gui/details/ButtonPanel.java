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
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.coor.conversion.CoordinateFormatManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.datatransfer.ClipboardUtils;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.ShortcutFactory;
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
import org.openstreetmap.josm.plugins.improveosm.observer.DownloadWayObservable;
import org.openstreetmap.josm.plugins.improveosm.observer.DownloadWayObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.builder.ButtonBuilder;


/**
 * Defines the {@code ImproveOsmDetailsDialog} action panel. The user can perform the following actions: filter the
 * data, comment/solve/reopen/invalidate the selected item(s).
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel implements DownloadWayObservable {

    private static final long serialVersionUID = -1004058684285756450L;

    private static final int ROWS = 1;
    private static final int COLS = 5;
    private static final Dimension DIM = new Dimension(200, 24);

    /* panel components */
    private final JButton btnComment;
    private final JButton btnSolve;
    private final JButton btnReopen;
    private final JButton btnInvalid;
    private final JButton btnDownload;

    private LatLon selectedItemCoordinate;

    private DownloadWayObserver downloadObserver;

    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        final JButton btnFilter = ButtonBuilder.build(new DisplayFilterDialog(), iconConfig.getFilterIcon(),
                guiConfig.getBtnFilterTlt(), true);
        btnComment =
                ButtonBuilder.build(
                        new DisplayEditDialogAction(guiConfig.getDlgCommentTitle(), guiConfig.getCommentShortcutTxt(),
                                null, iconConfig.getCommentIcon()),
                        iconConfig.getCommentIcon(), guiConfig.getBtnCommentTlt(), false);
        btnSolve =
                ButtonBuilder.build(
                        new DisplayEditPopupMenu(guiConfig.getDlgSolveTitle(), guiConfig.getMenuSolveCommentTitle(),
                                guiConfig.getSolveShortcutTxt(), guiConfig.getSolveCommentShortcutTxt(), Status.SOLVED,
                                iconConfig.getSolveIcon()),
                        iconConfig.getSolveIcon(), guiConfig.getBtnSolveTlt(), false);
        btnReopen =
                ButtonBuilder.build(
                        new DisplayEditPopupMenu(guiConfig.getDlgReopenTitle(), guiConfig.getMenuReopenCommentTitle(),
                                guiConfig.getReopenShortcutTxt(), guiConfig.getReopenCommentShortcutTxt(), Status.OPEN,
                                iconConfig.getReopenIcon()),
                        iconConfig.getReopenIcon(), guiConfig.getBtnReopenTlt(), false);
        btnInvalid =
                ButtonBuilder.build(
                        new DisplayEditPopupMenu(guiConfig.getDlgInvalidTitle(), guiConfig.getMenuInvalidCommentTitle(),
                                guiConfig.getInvalidShortcutTxt(), guiConfig.getInvalidCommentShortcutTxt(),
                                Status.INVALID, iconConfig.getInvalidIcon()),
                        iconConfig.getInvalidIcon(), guiConfig.getBtnInvalidTlt(), false);
        final JosmAction downloadAction = new DownloadWayId();
        final String tooltip = GuiConfig.getInstance().getBtnDownloadTlt().replace("sc",
                downloadAction.getShortcut().getKeyText());
        btnDownload = ButtonBuilder.build(new DownloadWayId(), IconConfig.getInstance().getDownloadIcon(), tooltip, false);
        
        final JButton btnLocation = ButtonBuilder.build(new HandleLocationAction(), iconConfig.getLocationIcon(),
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
        ((DisplayEditDialogAction) btnComment.getAction()).registerCommentObserver(commentObserver);
        ((DisplayEditPopupMenu) btnSolve.getAction()).registerCommentObserver(commentObserver);
        ((DisplayEditPopupMenu) btnReopen.getAction()).registerCommentObserver(commentObserver);
        ((DisplayEditPopupMenu) btnInvalid.getAction()).registerCommentObserver(commentObserver);
    }

    /**
     * Enables the panel actions based on the given status and location. By default only the filter action is enabled;
     * the other actions are enabled/disabled based on the selected item status.
     *
     * @param status the currently selected object/objects status
     * @param location the currently selected object/objects location
     */
    void enablePanelActions(final Status status, final LatLon location) {
        selectedItemCoordinate = location;
        ((DisplayEditPopupMenu) btnSolve.getAction()).setCurrentStatus(status);
        ((DisplayEditPopupMenu) btnReopen.getAction()).setCurrentStatus(status);
        ((DisplayEditPopupMenu) btnInvalid.getAction()).setCurrentStatus(status);
        if (status == null) {
            enablePanelActions(false, false, false, false);
            remove(btnDownload);
        } else {
            if (status == Status.OPEN) {
                enablePanelActions(true, true, false, true);
            } else {
                enablePanelActions(true, false, true, false);
            }
            setDownloadButtonAccess();
       
        }
        
    }
    
    public void setDownloadButtonAccess() {
      Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
      if(activeLayer instanceof DirectionOfFlowLayer && ((DirectionOfFlowLayer) activeLayer).hasSelectedItems()) {
          add(btnDownload);
          btnDownload.setEnabled(true);
      } else {
          remove(btnDownload);
      }
    }
    
    private void enablePanelActions(final boolean commmentFlag, final boolean solveFlag, final boolean reopenFlag,
            final boolean invalidFlag) {
        btnComment.setEnabled(commmentFlag);
        btnSolve.setEnabled(solveFlag);
        btnReopen.setEnabled(reopenFlag);
        btnInvalid.setEnabled(invalidFlag);
    }

    @Override
    public void addObserver(DownloadWayObserver observer) {
        downloadObserver = observer;
        
    }

    @Override
    public void notifyObserver() {
        downloadObserver.downloadWay();
        
    }

    /*
     * Displays the filters corresponding to the active data layer.
     */
    private final class DisplayFilterDialog extends AbstractAction {

        private static final long serialVersionUID = 3707557295874437209L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (MainApplication.getMap() != null) {
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
        private final EditPopupMenu menu;

        private DisplayEditPopupMenu(final String titleEdit, final String titleEditComment,
                final String editShortcutKey, final String editCommentShortcutKey, final Status status,
                final ImageIcon icon) {
            menu = new EditPopupMenu(titleEdit, titleEditComment, editShortcutKey, editCommentShortcutKey, status,
                    icon);
        }

        private void registerCommentObserver(final CommentObserver commentObserver) {
            menu.registerCommentObserver(commentObserver);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
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

        public void setCurrentStatus(final Status currentStatus) {
            menu.setCurrentStatus(currentStatus);
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
                    : MainApplication.getMap().mapView.getRealBounds().getCenter();
            final int zoom = org.openstreetmap.josm.plugins.improveosm.util.Util
                    .zoom(MainApplication.getMap().mapView.getRealBounds());

            switch (PreferenceManager.getInstance().loadLocationPrefOption()) {
                case OPEN_STREET_VIEW:
                    openUrl(Config.getInstance().getLocationPrefOpenStreetView(),
                            CoordinateFormatManager.getDefaultFormat().latToString(location),
                            CoordinateFormatManager.getDefaultFormat().lonToString(location), String.valueOf(zoom));
                    break;
                case CUSTOM_SITE:
                    openUrl(generateCustomURL(PreferenceManager.getInstance().loadLocationPrefValue()),
                            CoordinateFormatManager.getDefaultFormat().latToString(location),
                            CoordinateFormatManager.getDefaultFormat().lonToString(location), String.valueOf(zoom));
                    break;
                case COPY_LOCATION:
                    ClipboardUtils.copyString(Formatter.formatLatLon(location));
            }
        }

        private String generateCustomURL(final String url) {
            String finalUrl = null;
            final String[] patterns = Config.getInstance().getLocationPrefUrlPatterns();
            for (int i = 0; i < patterns.length; i++) {
                if (url.contains(patterns[i])) {
                    finalUrl = url + Config.getInstance().getLocationPrefUrlVariablePart()[i];
                    break;
                }
            }
            return finalUrl;
        }

        private void openUrl(final String url, final String... args) {
            String finalUrl = url;
            for (final String arg : args) {
                finalUrl = finalUrl.replaceFirst("_", arg);
            }
            if (finalUrl != null) {
                try {
                    OpenBrowser.displayUrl(new URI(finalUrl));
                } catch (final URISyntaxException e) {
                    // should not arrive here
                }
            }
        }
    }
    
    private final class DownloadWayId extends JosmAction{
        
        private static final long serialVersionUID = 1L;

        DownloadWayId() {
            super(GuiConfig.getInstance().getBtnDownloadTlt(), null, GuiConfig.getInstance().getBtnDownloadTlt(),
                    ShortcutFactory.getInstance().getShortcut(GuiConfig.getInstance().getBtnDownloadTlt()), true);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            notifyObserver();
        }
        
    }
}