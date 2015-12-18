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
            }
            addLayers(addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
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
            zoomTimer = new Timer(SEARCH_DELAY, new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent event) {
                    if (missingGeometryLayer != null && missingGeometryLayer.isVisible()) {
                        Main.worker.execute(new MissingGeometryUpdateThread());
                    }
                    if (directionOfFlowLayer != null && directionOfFlowLayer.isVisible()) {
                        Main.worker.execute(new DirectionOfFlowUpdateThread());
                    }
                    if (turnRestrictionLayer != null && turnRestrictionLayer.isVisible()) {
                        Main.worker.execute(new TurnRestrictionUpdateThread());
                    }
                    new InfoDialog().displayOldPluginsDialog();
                }
            });
            zoomTimer.setRepeats(false);
            zoomTimer.start();
        }
    }

    /* PreferenceChangeListener method */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            switch (event.getKey()) {
                case Keys.MISSINGGEO_FILTERS_CHANGED:
                    Main.worker.execute(new MissingGeometryUpdateThread());
                    break;
                case Keys.DIRECTIONOFFLOW_FILTERS_CHANGED:
                    Main.worker.execute(new DirectionOfFlowUpdateThread());
                    break;
                case Keys.TURN_RESTRICTION_FILTERS_CHANGED:
                    Main.worker.execute(new TurnRestrictionUpdateThread());
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
        boolean addMissingGeometry = false;
        boolean addDirectionOfFlow = false;
        boolean addTurnRestriction = false;
        if (dataLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            addMissingGeometry = missingGeometryLayer == null;
        }
        if (dataLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            addDirectionOfFlow = directionOfFlowLayer == null;
        }
        if (dataLayers.contains(DataLayer.TURN_RESTRICTION)) {
            addTurnRestriction = turnRestrictionLayer == null;
        }
        addDialogWindows(Main.map, addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
        addLayers(addMissingGeometry, addDirectionOfFlow, addTurnRestriction);
    }

    /* MouseListener implementation */

    @Override
    public void mouseClicked(final MouseEvent event) {
        final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
        final Layer activeLayer = Main.map.mapView.getActiveLayer();
        if (SwingUtilities.isLeftMouseButton(event)) {
            if (activeLayer == missingGeometryLayer && missingGeometryLayer.isVisible()
                    && zoom > Config.getMissingGeometryInstance().getMaxClusterZoom()) {
                // select tiles
                selectTile(event.getPoint(), event.isShiftDown());
            } else if (activeLayer == directionOfFlowLayer && directionOfFlowLayer.isVisible()
                    && zoom > Config.getDirectionOfFlowInstance().getMaxClusterZoom()) {
                // select road segments
                selectRoadSegment(event.getPoint(), event.isShiftDown());
            } else if (activeLayer == turnRestrictionLayer && turnRestrictionLayer.isVisible()
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
                updateMissingGeometrySelectedData(tile, comments);
            }
        } else {
            // clear selection
            updateMissingGeometrySelectedData(null, null);
        }
    }

    private void selectRoadSegment(final Point point, final boolean multiSelect) {
        final RoadSegment roadSegment = directionOfFlowLayer.nearbyItem(point, multiSelect);
        if (roadSegment != null) {
            if (!roadSegment.equals(directionOfFlowLayer.lastSelectedItem())) {
                final List<Comment> comments = ServiceHandler.getDirectionOfFlowHandler().retrieveComments(roadSegment);
                updateDirectionOfFlowSelectedData(roadSegment, comments);
            }
        } else if (!multiSelect) {
            // clear selection
            updateDirectionOfFlowSelectedData(null, null);
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
                    shouldSelect = true;
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
            updateTurnRestrictionSelectedData(turnRestriction, comments);
        } else if (!multiSelect) {
            // clear selection
            updateTurnRestrictionSelectedData(null, null);
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

    @Override
    public void selectItem(final TurnRestriction turnRestriction) {
        final List<Comment> comments = ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                turnRestrictionDialog.updateUI(turnRestriction, comments);
                turnRestrictionLayer.updateSelectedItem(null);
                turnRestrictionLayer.updateSelectedItem(turnRestriction);
                Main.map.repaint();
            }
        });
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
            Main.worker.execute(new MissingGeometryUpdateThread());
        }
        final Status statusFilter = PreferenceManager.getInstance().loadMissingGeometryFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getMissingGeometryHandler().retrieveComments(items.get(items.size() - 1));
            updateMissingGeometrySelectedData(items.get(items.size() - 1), comments);
        } else {
            updateMissingGeometrySelectedData(null, null);
        }
    }

    private void commentRoadSegments(final Comment comment, final List<RoadSegment> items) {
        PreferenceManager.getInstance().saveDirectionOfFlowLastComment(comment.getText());
        ServiceHandler.getDirectionOfFlowHandler().comment(comment, items);
        // reload data
        if (comment.getStatus() != null) {
            Main.worker.execute(new DirectionOfFlowUpdateThread());
        }
        final Status statusFilter = PreferenceManager.getInstance().loadOnewayFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getDirectionOfFlowHandler().retrieveComments(items.get(items.size() - 1));
            updateDirectionOfFlowSelectedData(items.get(items.size() - 1), comments);
        } else {
            updateDirectionOfFlowSelectedData(null, null);
        }
    }

    private void commentTurnRestrictions(final Comment comment, final List<TurnRestriction> items) {
        PreferenceManager.getInstance().saveTurnRestrictionLastComment(comment.getText());
        ServiceHandler.getTurnRestrictionHandler().comment(comment, items);
        // reload data
        if (comment.getStatus() != null) {
            Main.worker.execute(new TurnRestrictionUpdateThread());
        }
        final Status statusFilter = PreferenceManager.getInstance().loadTurnRestrictionFilter().getStatus();
        if (comment.getStatus() == null || statusFilter == null || (comment.getStatus() == statusFilter)) {
            final List<Comment> comments =
                    ServiceHandler.getTurnRestrictionHandler().retrieveComments(items.get(items.size() - 1));
            updateTurnRestrictionSelectedData(items.get(items.size() - 1), comments);
        } else {
            updateTurnRestrictionSelectedData(null, null);
        }
    }

    /* commonly used private methods and classes */

    private void addDialogWindows(final MapFrame newMapFrame, final boolean addMissingGeometry,
            final boolean addDirectionOfFlow, final boolean addTurnRestriction) {
        // MissingGeometry functionality
        if (addMissingGeometry) {
            missingGeometryDialog = new MissingGeometryDetailsDialog();
            newMapFrame.addToggleDialog(missingGeometryDialog);
            missingGeometryDialog.getButton().setActionCommand(MissingGeometryGuiConfig.getInstance().getLayerName());
            missingGeometryDialog.getButton().addActionListener(new ToggleButtonActionListener());
            missingGeometryDialog.getButton().setSelected(true);
            missingGeometryDialog.registerCommentObserver(this);
        }

        // DirectionOfFlow functionality
        if (addDirectionOfFlow) {
            directionOfFlowDialog = new DirectionOfFlowDetailsDialog();
            newMapFrame.addToggleDialog(directionOfFlowDialog);
            directionOfFlowDialog.getButton().setActionCommand(DirectionOfFlowGuiConfig.getInstance().getLayerName());
            directionOfFlowDialog.getButton().addActionListener(new ToggleButtonActionListener());
            directionOfFlowDialog.getButton().setSelected(true);
            directionOfFlowDialog.registerCommentObserver(this);
        }

        if (addTurnRestriction) {
            turnRestrictionDialog = new TurnRestrictionDetailsDialog();
            newMapFrame.addToggleDialog(turnRestrictionDialog);
            turnRestrictionDialog.getButton().setActionCommand(TurnRestrictionGuiConfig.getInstance().getLayerName());
            turnRestrictionDialog.getButton().addActionListener(new ToggleButtonActionListener());
            turnRestrictionDialog.getButton().setSelected(true);
            turnRestrictionDialog.registerCommentObserver(this);
            turnRestrictionDialog.registerSelectionObserver(this);
        }
    }

    private void addLayers(final boolean addMissingGeometry, final boolean addDirectionOfFlow,
            final boolean addTurnRestriction) {
        if (!listenersRegistered) {
            NavigatableComponent.addZoomChangeListener(this);
            MapView.addLayerChangeListener(this);
            Main.pref.addPreferenceChangeListener(this);
            Main.map.mapView.addMouseListener(this);
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

    private void updateMissingGeometrySelectedData(final Tile tile, final List<Comment> comments) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                missingGeometryDialog.updateUI(tile, comments);
                missingGeometryLayer.updateSelectedItem(tile);
                Main.map.repaint();
            }
        });
    }

    private void updateDirectionOfFlowSelectedData(final RoadSegment roadSegment, final List<Comment> comments) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                directionOfFlowDialog.updateUI(roadSegment, comments);
                directionOfFlowLayer.updateSelectedItem(roadSegment);
                Main.map.repaint();
            }
        });
    }

    private void updateTurnRestrictionSelectedData(final TurnRestriction roadSegment, final List<Comment> comments) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                turnRestrictionDialog.updateUI(roadSegment, comments);
                turnRestrictionLayer.updateSelectedItem(roadSegment);
                Main.map.repaint();
            }
        });
    }

    private class MissingGeometryUpdateThread implements Runnable {

        @Override
        public void run() {
            if (Main.map != null && Main.map.mapView != null) {
                final BoundingBox bbox = new BoundingBox(Main.map.mapView);
                if (bbox != null) {
                    final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
                    final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
                    final DataSet<Tile> result = ServiceHandler.getMissingGeometryHandler().search(bbox, filter, zoom);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            missingGeometryLayer.setDataSet(result);
                            updateSelection(result);
                            Main.map.repaint();
                        }
                    });
                }
            }
        }

        private void updateSelection(final DataSet<Tile> result) {
            if (result != null) {
                final Tile tile = missingGeometryLayer.lastSelectedItem();
                if (tile != null) {
                    if (missingGeometryDialog.reloadComments()) {
                        final List<Comment> comments =
                                ServiceHandler.getMissingGeometryHandler().retrieveComments(tile);
                        missingGeometryDialog.updateUI(tile, comments);
                    } else {
                        missingGeometryDialog.updateUI(tile);
                    }
                } else {
                    missingGeometryDialog.updateUI(null, null);
                }
            }
        }
    }

    private class DirectionOfFlowUpdateThread implements Runnable {

        @Override
        public void run() {
            if (Main.map != null && Main.map.mapView != null) {
                final BoundingBox bbox = new BoundingBox(Main.map.mapView);
                if (bbox != null) {
                    final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
                    final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
                    final DataSet<RoadSegment> result =
                            ServiceHandler.getDirectionOfFlowHandler().search(bbox, filter, zoom);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            new InfoDialog().displayDialog(zoom);
                            directionOfFlowLayer.setDataSet(result);
                            updateSelection(result);
                            Main.map.repaint();
                        }
                    });

                }
            }
        }

        private void updateSelection(final DataSet<RoadSegment> result) {
            if (result != null) {
                final RoadSegment roadSegment = directionOfFlowLayer.lastSelectedItem();
                if (!result.getClusters().isEmpty()) {
                    // clear segment details
                    directionOfFlowDialog.updateUI(null, null);
                } else if (roadSegment != null) {
                    if (directionOfFlowDialog.reloadComments()) {
                        final List<Comment> comments =
                                ServiceHandler.getDirectionOfFlowHandler().retrieveComments(roadSegment);
                        directionOfFlowDialog.updateUI(roadSegment, comments);
                    } else {
                        directionOfFlowDialog.updateUI(roadSegment);
                    }
                } else {
                    directionOfFlowDialog.updateUI(null, null);
                }
            }
        }
    }

    private class TurnRestrictionUpdateThread implements Runnable {

        @Override
        public void run() {
            if (Main.map != null && Main.map.mapView != null) {
                final BoundingBox bbox = new BoundingBox(Main.map.mapView);
                if (bbox != null) {
                    final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
                    final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
                    final DataSet<TurnRestriction> result =
                            ServiceHandler.getTurnRestrictionHandler().search(bbox, filter, zoom);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            turnRestrictionLayer.setDataSet(result);
                            updateSelection(result);
                            Main.map.repaint();
                        }
                    });

                }
            }
        }

        private void updateSelection(final DataSet<TurnRestriction> result) {
            if (result != null) {
                final TurnRestriction turnRestriction = turnRestrictionLayer.lastSelectedItem();
                if (!result.getClusters().isEmpty()) {
                    // clear segment details
                    turnRestrictionDialog.updateUI(null, null);
                } else if (turnRestriction != null) {
                    if (turnRestriction.getTurnRestrictions() == null && turnRestrictionDialog.reloadComments()) {
                        final List<Comment> comments =
                                ServiceHandler.getTurnRestrictionHandler().retrieveComments(turnRestriction);
                        turnRestrictionDialog.updateUI(turnRestriction, comments);
                    } else {
                        turnRestrictionDialog.updateUI(turnRestriction);
                    }
                } else {
                    turnRestrictionDialog.updateUI(null, null);
                }
            }
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