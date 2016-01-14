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
package org.openstreetmap.josm.plugins.improveosm;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.MapView.LayerChangeListener;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.InfoDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.DirectionOfFlowDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.Keys;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the main functionality of the Improve-OSM plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class ImproveOsmPlugin extends Plugin implements LayerChangeListener, ZoomChangeListener,
PreferenceChangedListener, MouseListener, CommentObserver, TurnRestrictionSelectionObserver {

    /* layers associated with this plugin */
    private MissingGeometryLayer missingGeometryLayer;
    private DirectionOfFlowLayer directionOfFlowLayer;
    private TurnRestrictionLayer turnRestrictionLayer;

    /* toggle dialogs associated with this plugin */
    private MissingGeometryDetailsDialog missingGeometryDialog;
    private DirectionOfFlowDetailsDialog directionOfFlowDialog;
    private TurnRestrictionDetailsDialog turnRestrictionDialog;

    private static final int SEARCH_DELAY = 600;
    private Timer zoomTimer;
    private boolean listenersRegistered = false;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInfo} object
     */
    public ImproveOsmPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
    }


    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null) {
            final EnumSet<DataLayer> dataLayers = PreferenceManager.getInstance().loadDataLayers();
            final boolean addMissingGeometry = dataLayers.contains(DataLayer.MISSING_GEOMETRY);
            final boolean addDirectionOfFlow = dataLayers.contains(DataLayer.DIRECTION_OF_FLOW);
            final boolean addTurnRestriction = dataLayers.contains(DataLayer.TURN_RESTRICTION);
            if (!GraphicsEnvironment.isHeadless()) {
                addDialogWindows(newMapFrame, addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
                addLayers(addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
            }
        }
    }


    /* implementation of LayerChangeListener */

    @Override
    public void activeLayerChange(final Layer layer1, final Layer layer2) {
        // nothing to add here
    }

    @Override
    public void layerAdded(final Layer newLayer) {
        if (newLayer instanceof ImproveOsmLayer) {
            zoomChanged();
        }
    }

    @Override
    public void layerRemoved(final Layer currentLayer) {
        if (currentLayer instanceof MissingGeometryLayer) {
            missingGeometryDialog.hideDialog();
            Main.main.removeLayer(missingGeometryLayer);
            missingGeometryLayer = null;
        } else if (currentLayer instanceof DirectionOfFlowLayer) {
            directionOfFlowDialog.hideDialog();
            Main.main.removeLayer(directionOfFlowLayer);
            directionOfFlowLayer = null;
        } else if (currentLayer instanceof TurnRestrictionLayer) {
            turnRestrictionDialog.hideDialog();
            Main.main.removeLayer(turnRestrictionLayer);
            turnRestrictionLayer = null;
        }
        if (missingGeometryLayer == null && directionOfFlowLayer == null && turnRestrictionLayer == null) {
            // remove listeners
            PreferenceManager.getInstance().saveErrorSuppressFlag(false);
            MapView.removeLayerChangeListener(this);
            NavigatableComponent.removeZoomChangeListener(this);
            Main.pref.removePreferenceChangeListener(this);
            Main.map.mapView.removeMouseListener(this);
            listenersRegistered = false;
        }
    }


    /* ZoomChangeListener method */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            // if timer is running restart it
            zoomTimer.restart();
        } else {
            zoomTimer = new Timer(SEARCH_DELAY, new ZoomActionListener());
            zoomTimer.setRepeats(false);
            zoomTimer.start();
        }
    }


    private final class ZoomActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (missingGeometryLayer != null && missingGeometryLayer.isVisible()) {
                Main.worker.execute(new MissingGeometryUpdateThread(missingGeometryDialog, missingGeometryLayer));
            }
            if (directionOfFlowLayer != null && directionOfFlowLayer.isVisible()) {
                Main.worker.execute(new DirectionOfFlowUpdateThread(directionOfFlowDialog, directionOfFlowLayer));
            }
            if (turnRestrictionLayer != null && turnRestrictionLayer.isVisible()) {
                Main.worker.execute(new TurnRestrictionUpdateThread(turnRestrictionDialog, turnRestrictionLayer));
            }
            new InfoDialog().displayOldPluginsDialog();
        }
    }


    /* PreferenceChangeListener method */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            switch (event.getKey()) {
                case Keys.MISSINGGEO_FILTERS_CHANGED:
                    Main.worker.execute(new MissingGeometryUpdateThread(missingGeometryDialog, missingGeometryLayer));
                    break;
                case Keys.DIRECTIONOFFLOW_FILTERS_CHANGED:
                    Main.worker.execute(new DirectionOfFlowUpdateThread(directionOfFlowDialog, directionOfFlowLayer));
                    break;
                case Keys.TURN_RESTRICTION_FILTERS_CHANGED:
                    Main.worker.execute(new TurnRestrictionUpdateThread(turnRestrictionDialog, turnRestrictionLayer));
                    break;
                case Keys.DATA_LAYER:
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            updateActiveLayers();
                        }
                    });
                    break;
                default:
                    // ignore it
                    break;
            }
        }
    }

    private void updateActiveLayers() {
        final EnumSet<DataLayer> dataLayers = PreferenceManager.getInstance().loadDataLayers();
        final boolean addMissingGeometry =
                dataLayers.contains(DataLayer.MISSING_GEOMETRY) && missingGeometryLayer == null;
        final boolean addDirectionOfFlow =
                dataLayers.contains(DataLayer.DIRECTION_OF_FLOW) && directionOfFlowLayer == null;
        final boolean addTurnRestriction =
                dataLayers.contains(DataLayer.TURN_RESTRICTION) && turnRestrictionLayer == null;
        addDialogWindows(Main.map, addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
        addLayers(addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
    }


    /* MouseListener implementation */

    @Override
    public void mouseClicked(final MouseEvent event) {
        final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
        final Layer activeLayer = Main.map.mapView.getActiveLayer();
        if (SwingUtilities.isLeftMouseButton(event)) {
            if (activeLayer instanceof MissingGeometryLayer
                    && zoom > Config.getMissingGeometryInstance().getMaxClusterZoom()) {
                // select tiles
                selectTile(event.getPoint(), event.isShiftDown());
            } else if (activeLayer instanceof DirectionOfFlowLayer
                    && zoom > Config.getDirectionOfFlowInstance().getMaxClusterZoom()) {
                // select road segments
                selectRoadSegment(event.getPoint(), event.isShiftDown());
            } else if (activeLayer instanceof TurnRestrictionLayer
                    && zoom > Config.getTurnRestrictionInstance().getMaxClusterZoom()) {
                selectTurnRestriction(event.getPoint(), event.isShiftDown());
            }
        }
    }

    private void selectTile(final Point point, final boolean multiSelect) {
        final Tile tile = missingGeometryLayer.nearbyItem(point, multiSelect);
        if (tile != null) {
            if (!tile.equals(missingGeometryLayer.lastSelectedItem())) {
                final List<Comment> comments = ServiceHandler.getMissingGeometryHandler().retrieveComments(tile);
                updateSelectedData(missingGeometryDialog, missingGeometryLayer, tile, comments);
            }
        } else {
            // clear selection
            updateSelectedData(missingGeometryDialog, missingGeometryLayer, null, null);
        }
    }

    private void selectRoadSegment(final Point point, final boolean multiSelect) {
        final RoadSegment roadSegment = directionOfFlowLayer.nearbyItem(point, multiSelect);
        if (roadSegment != null) {
            if (!roadSegment.equals(directionOfFlowLayer.lastSelectedItem())) {
                final List<Comment> comments = ServiceHandler.getDirectionOfFlowHandler().retrieveComments(roadSegment);
                updateSelectedData(directionOfFlowDialog, directionOfFlowLayer, roadSegment, comments);
            }
        } else if (!multiSelect) {
            // clear selection
            updateSelectedData(directionOfFlowDialog, directionOfFlowLayer, null, null);
        }
    }

    private void selectTurnRestriction(final Point point, final boolean multiSelect) {
        final TurnRestriction turnRestriction = turnRestrictionLayer.nearbyItem(point, multiSelect);
        boolean shouldSelect = false;
        if (turnRestriction != null) {
            if (multiSelect) {
                if (turnRestriction.getTurnRestrictions() != null) {
                    shouldSelect = turnRestrictionLayer.getSelectedItems().isEmpty();
                } else {
                    final TurnRestriction lastSelectedItem = turnRestrictionLayer.lastSelectedItem();
                    if ((lastSelectedItem == null)
                            || (lastSelectedItem != null && lastSelectedItem.getTurnRestrictions() == null)) {
                        shouldSelect = true;
                    }
                }
            } else {
                shouldSelect = true;
            }
        }
        if (shouldSelect) {
            List<Comment> comments = null;
            if (turnRestriction.getTurnRestrictions() == null
                    && !turnRestriction.equals(turnRestrictionLayer.lastSelectedItem())) {
                comments = ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);
            }
            updateSelectedData(turnRestrictionDialog, turnRestrictionLayer, turnRestriction, comments);
        } else if (!multiSelect) {
            // clear selection
            updateSelectedData(turnRestrictionDialog, turnRestrictionLayer, null, null);
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        // no logic for this action
    }


    /* TurnRestrictionSelectionObserver implementation */

    @Override
    public void selectItem(final TurnRestriction turnRestriction) {
        final List<Comment> comments = ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);
        turnRestrictionLayer.updateSelectedItem(null);
        updateSelectedData(turnRestrictionDialog, turnRestrictionLayer, turnRestriction, comments);
    }


    /* CommentObserver method */

    @Override
    public void createComment(final Comment comment) {
        final Layer activeLayer = Main.map.mapView.getActiveLayer();
        if (activeLayer == missingGeometryLayer) {
            final List<Tile> selectedTiles = missingGeometryLayer.getSelectedItems();
            Main.worker.execute(new Runnable() {

                @Override
                public void run() {
                    commentTiles(comment, selectedTiles);
                }
            });
        } else if (activeLayer == directionOfFlowLayer) {
            final List<RoadSegment> selectedRoadSegments = directionOfFlowLayer.getSelectedItems();
            Main.worker.execute(new Runnable() {

                @Override
                public void run() {
                    commentRoadSegments(comment, selectedRoadSegments);
                }
            });
        } else if (activeLayer == turnRestrictionLayer) {
            final List<TurnRestriction> selectedTurnRestrictions = turnRestrictionLayer.getSelectedItems();
            Main.worker.execute(new Runnable() {

                @Override
                public void run() {
                    commentTurnRestrictions(comment, selectedTurnRestrictions);
                }
            });
        }

    }

    private void commentTiles(final Comment comment, final List<Tile> items) {
        PreferenceManager.getInstance().saveMissingGeometryLastComment(comment.getText());
        ServiceHandler.getMissingGeometryHandler().comment(comment, items);
        // reload data
        if (comment.getStatus() != null) {
            Main.worker.execute(new MissingGeometryUpdateThread(missingGeometryDialog, missingGeometryLayer));
        }
        final Status statusFilter = PreferenceManager.getInstance().loadMissingGeometryFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getMissingGeometryHandler().retrieveComments(items.get(items.size() - 1));
            updateSelectedData(missingGeometryDialog, missingGeometryLayer, items.get(items.size() - 1), comments);
        } else {
            updateSelectedData(missingGeometryDialog, missingGeometryLayer, null, null);
        }
    }

    private void commentRoadSegments(final Comment comment, final List<RoadSegment> items) {
        PreferenceManager.getInstance().saveDirectionOfFlowLastComment(comment.getText());
        ServiceHandler.getDirectionOfFlowHandler().comment(comment, items);
        // reload data
        if (comment.getStatus() != null) {
            Main.worker.execute(new DirectionOfFlowUpdateThread(directionOfFlowDialog, directionOfFlowLayer));
        }
        final Status statusFilter = PreferenceManager.getInstance().loadOnewayFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getDirectionOfFlowHandler().retrieveComments(items.get(items.size() - 1));
            updateSelectedData(directionOfFlowDialog, directionOfFlowLayer, items.get(items.size() - 1), comments);
        } else {
            updateSelectedData(directionOfFlowDialog, directionOfFlowLayer, null, null);
        }
    }

    private void commentTurnRestrictions(final Comment comment, final List<TurnRestriction> items) {
        PreferenceManager.getInstance().saveTurnRestrictionLastComment(comment.getText());
        ServiceHandler.getTurnRestrictionHandler().comment(comment, items);
        // reload data
        if (comment.getStatus() != null) {
            Main.worker.execute(new TurnRestrictionUpdateThread(turnRestrictionDialog, turnRestrictionLayer));
        }
        final Status statusFilter = PreferenceManager.getInstance().loadTurnRestrictionFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getTurnRestrictionHandler().retrieveComments(items.get(items.size() - 1));
            updateSelectedData(turnRestrictionDialog, turnRestrictionLayer, items.get(items.size() - 1), comments);
        } else {
            updateSelectedData(turnRestrictionDialog, turnRestrictionLayer, null, null);
        }
    }


    /* commonly used private methods and classes */

    private void addDialogWindows(final MapFrame newMapFrame, final boolean addMissingGeometry,
            final boolean addDirectionOfFlow, final boolean addTurnRestriction) {
        // MissingGeometry functionality
        if (addMissingGeometry) {
            missingGeometryDialog = new MissingGeometryDetailsDialog();
            newMapFrame.addToggleDialog(missingGeometryDialog);
            setUpDialogWindow(missingGeometryDialog, MissingGeometryGuiConfig.getInstance().getLayerName());
        }

        // DirectionOfFlow functionality
        if (addDirectionOfFlow) {
            directionOfFlowDialog = new DirectionOfFlowDetailsDialog();
            newMapFrame.addToggleDialog(directionOfFlowDialog);
            setUpDialogWindow(directionOfFlowDialog, DirectionOfFlowGuiConfig.getInstance().getLayerName());
        }

        // TurnRestriction functionality
        if (addTurnRestriction) {
            turnRestrictionDialog = new TurnRestrictionDetailsDialog();
            newMapFrame.addToggleDialog(turnRestrictionDialog);
            setUpDialogWindow(turnRestrictionDialog, TurnRestrictionGuiConfig.getInstance().getLayerName());
            turnRestrictionDialog.registerSelectionObserver(this);
        }
    }

    private <T> void setUpDialogWindow(final ImproveOsmDetailsDialog<T> dialog, final String actionCommand) {
        dialog.getButton().setActionCommand(actionCommand);
        dialog.getButton().addActionListener(new ToggleButtonActionListener());
        dialog.getButton().setSelected(true);
        dialog.registerCommentObserver(this);
    }

    private void addLayers(final boolean addMissingGeometry, final boolean addDirectionOfFlow,
            final boolean addTurnRestriction) {
        if (!listenersRegistered) {
            NavigatableComponent.addZoomChangeListener(this);
            MapView.addLayerChangeListener(this);
            Main.pref.addPreferenceChangeListener(this);
            Main.map.mapView.addMouseListener(this);
            listenersRegistered = true;
        }
        if (addMissingGeometry) {
            missingGeometryLayer = new MissingGeometryLayer();
            Main.main.addLayer(missingGeometryLayer);
            missingGeometryDialog.showDialog();
        }

        if (addDirectionOfFlow) {
            directionOfFlowLayer = new DirectionOfFlowLayer();
            Main.main.addLayer(directionOfFlowLayer);
            directionOfFlowDialog.showDialog();
        }
        if (addTurnRestriction) {
            turnRestrictionLayer = new TurnRestrictionLayer();
            Main.main.addLayer(turnRestrictionLayer);
            turnRestrictionDialog.showDialog();
        }
    }

    private <T> void updateSelectedData(final ImproveOsmDetailsDialog<T> dialog, final ImproveOsmLayer<T> layer,
            final T item, final List<Comment> comments) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                dialog.updateUI(item, comments);
                layer.updateSelectedItem(item);
                Main.map.repaint();
            }
        });
    }

    private final class MissingGeometryUpdateThread extends UpdateThread<Tile> {

        private MissingGeometryUpdateThread(final ImproveOsmDetailsDialog<Tile> detailsDialog,
                final ImproveOsmLayer<Tile> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<Tile> searchData(final BoundingBox bbox, final int zoom) {
            final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
            return ServiceHandler.getMissingGeometryHandler().search(bbox, filter, zoom);
        }

        @Override
        List<Comment> retrieveComments(final Tile item) {
            return ServiceHandler.getMissingGeometryHandler().retrieveComments(item);
        }

    }

    private final class DirectionOfFlowUpdateThread extends UpdateThread<RoadSegment> {

        private DirectionOfFlowUpdateThread(final ImproveOsmDetailsDialog<RoadSegment> detailsDialog,
                final ImproveOsmLayer<RoadSegment> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<RoadSegment> searchData(final BoundingBox bbox, final int zoom) {
            final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
            return ServiceHandler.getDirectionOfFlowHandler().search(bbox, filter, zoom);
        }

        @Override
        List<Comment> retrieveComments(final RoadSegment item) {
            return ServiceHandler.getDirectionOfFlowHandler().retrieveComments(item);
        }
    }

    private final class TurnRestrictionUpdateThread extends UpdateThread<TurnRestriction> {

        private TurnRestrictionUpdateThread(final ImproveOsmDetailsDialog<TurnRestriction> detailsDialog,
                final ImproveOsmLayer<TurnRestriction> layer) {
            super(detailsDialog, layer);
        }

        @Override
        DataSet<TurnRestriction> searchData(final BoundingBox bbox, final int zoom) {
            final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
            return ServiceHandler.getTurnRestrictionHandler().search(bbox, filter, zoom);
        }

        @Override
        List<Comment> retrieveComments(final TurnRestriction item) {
            return item.getTurnRestrictions() == null
                    ? ServiceHandler.getTurnRestrictionHandler().retrieveComments(item) : null;
        }

    }

    private class ToggleButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            final IconToggleButton btn = (IconToggleButton) event.getSource();
            if (btn.isSelected()) {
                btn.setSelected(true);
            } else {
                btn.setSelected(false);
                btn.setFocusable(false);
            }
            final String actionCommand = event.getActionCommand();
            final boolean addMissingGeometry =
                    actionCommand.equals(MissingGeometryGuiConfig.getInstance().getLayerName())
                    && missingGeometryLayer == null;
            final boolean addDirectionOfFlow =
                    actionCommand.equals(DirectionOfFlowGuiConfig.getInstance().getLayerName())
                    && directionOfFlowLayer == null;
            final boolean addTurnRestriction =
                    actionCommand.equals(TurnRestrictionGuiConfig.getInstance().getLayerName())
                    && turnRestrictionLayer == null;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    addLayers(addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
                }
            });
        }
    }
}