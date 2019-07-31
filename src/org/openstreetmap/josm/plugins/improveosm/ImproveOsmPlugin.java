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

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;
import org.openstreetmap.josm.gui.datatransfer.ClipboardUtils;
import org.openstreetmap.josm.gui.layer.AbstractMapViewPaintable;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerAddEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerOrderChangeEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerRemoveEvent;
import org.openstreetmap.josm.gui.layer.MainLayerManager.ActiveLayerChangeEvent;
import org.openstreetmap.josm.gui.layer.MainLayerManager.ActiveLayerChangeListener;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.InfoDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.AbstractLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.DownloadWayObserver;
import org.openstreetmap.josm.plugins.improveosm.tread.DirectionOfFlowUpdateThread;
import org.openstreetmap.josm.plugins.improveosm.tread.MissingGeometryUpdateThread;
import org.openstreetmap.josm.plugins.improveosm.tread.TurnRestrictionUpdateThread;
import org.openstreetmap.josm.plugins.improveosm.tread.UpdateThread;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import org.openstreetmap.josm.spi.preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.spi.preferences.PreferenceChangedListener;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Logging;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Defines the main functionality of the Improve-OSM plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class ImproveOsmPlugin extends Plugin
        implements LayerChangeListener, ZoomChangeListener, PreferenceChangedListener, MouseListener, CommentObserver,
        TurnRestrictionSelectionObserver, DownloadWayObserver, ActiveLayerChangeListener {

    /* layers associated with this plugin */
    private MissingGeometryLayer missingGeometryLayer;
    private DirectionOfFlowLayer directionOfFlowLayer;
    private TurnRestrictionLayer turnRestrictionLayer;
    private TemporarySelectionLayer itemSelectionLayer;

    /* toggle dialog associated with this plugin */
    private ImproveOsmDetailsDialog detailsDialog;

    private static final int SEARCH_DELAY = 600;
    private Timer zoomTimer;
    private boolean listenersRegistered = false;

    /* menu items for the layers */
    private JMenuItem missingGeometryLayerMenuItem;
    private JMenuItem directionOfFlowLayerMenuItem;
    private JMenuItem turnRestrictionLayerLayerMenuItem;

    /* bounding box coordinates for multiple selection */
    private Point startDrag;
    private Point endDrag;


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
        if (MainApplication.getMap() != null && !GraphicsEnvironment.isHeadless()) {
            // create details dialog
            initializeDetailsDialog(newMapFrame);
            initializeLayerMenuItems();
            initializeLayers();
        }
        if (oldMapFrame != null && newMapFrame == null) {
            missingGeometryLayerMenuItem.setEnabled(false);
            directionOfFlowLayerMenuItem.setEnabled(false);
            turnRestrictionLayerLayerMenuItem.setEnabled(false);
            try {
                ThreadPool.getInstance().shutdown();
            } catch (final InterruptedException e) {
                Logging.error("Could not shutdown thead pool.", e);
            }
            MainApplication.getLayerManager().removeActiveLayerChangeListener(this);
        }
    }

    private void initializeDetailsDialog(final MapFrame newMapFrame) {
        
        MainApplication.getLayerManager().addActiveLayerChangeListener(this);
        // create details dialog
        detailsDialog = new ImproveOsmDetailsDialog();
        detailsDialog.registerCommentObserver(this);
        detailsDialog.registerTurnRestrictionSelectionObserver(this);
        detailsDialog.addDownloadWayButtonObserver(this);
        newMapFrame.addToggleDialog(detailsDialog);

        // enable dialog
        if (PreferenceManager.getInstance().loadPanelOpenedFlag()) {
            detailsDialog.showDialog();
        } else {
            detailsDialog.hideDialog();
        }
    }

    private void initializeLayers() {
        final Set<DataLayer> enabledDayaLayers = Config.getInstance().getEnabledDataLayers();
        if (PreferenceManager.getInstance().loadMissingGeometryLayerOpenedFlag()
                && enabledDayaLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            missingGeometryLayer = new MissingGeometryLayer();
            MainApplication.getLayerManager().addLayer(missingGeometryLayer);
        }
        if (PreferenceManager.getInstance().loadDirectionOfFlowLayerOpenedFlag()
                && enabledDayaLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            directionOfFlowLayer = new DirectionOfFlowLayer();
            MainApplication.getLayerManager().addLayer(directionOfFlowLayer);
        }
        if (PreferenceManager.getInstance().loadTurnRestrictionLayerOpenedFlag()
                && enabledDayaLayers.contains(DataLayer.TURN_RESTRICTION)) {
            turnRestrictionLayer = new TurnRestrictionLayer();
            MainApplication.getLayerManager().addLayer(turnRestrictionLayer);
        }
        if (missingGeometryLayer != null || directionOfFlowLayer != null || turnRestrictionLayer != null) {
            registerListeners();
        }
        itemSelectionLayer = new TemporarySelectionLayer();
    }

    private void registerListeners() {
        if (!listenersRegistered) {
            NavigatableComponent.addZoomChangeListener(ImproveOsmPlugin.this);
            MainApplication.getLayerManager().addLayerChangeListener(ImproveOsmPlugin.this);
            org.openstreetmap.josm.spi.preferences.Config.getPref().addPreferenceChangeListener(ImproveOsmPlugin.this);
            MainApplication.getMap().mapView.addMouseListener(ImproveOsmPlugin.this);
            MainApplication.getMap().mapView.addMouseMotionListener(new LayerSelectionListener());
            MainApplication.getMap().mapView.registerKeyboardAction(new CopyAction(),
                    GuiConfig.getInstance().getLblCopy(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                    JComponent.WHEN_FOCUSED);
            listenersRegistered = true;
        }
    }

    private void initializeLayerMenuItems() {
        final Set<DataLayer> enabledDayaLayers = Config.getInstance().getEnabledDataLayers();
        if (enabledDayaLayers.contains(DataLayer.MISSING_GEOMETRY)) {
            if (missingGeometryLayerMenuItem == null) {
                missingGeometryLayerMenuItem = MainMenu.add(MainApplication.getMenu().imageryMenu,
                        new LayerActivator(DataLayer.MISSING_GEOMETRY,
                                MissingGeometryGuiConfig.getInstance().getLayerName(),
                                IconConfig.getInstance().getMissingGeometryLayerIconName()),
                        false);
            }
            missingGeometryLayerMenuItem.setEnabled(true);
        }

        if (enabledDayaLayers.contains(DataLayer.DIRECTION_OF_FLOW)) {
            if (directionOfFlowLayerMenuItem == null) {
                directionOfFlowLayerMenuItem = MainMenu.add(MainApplication.getMenu().imageryMenu,
                        new LayerActivator(DataLayer.DIRECTION_OF_FLOW,
                                DirectionOfFlowGuiConfig.getInstance().getLayerName(),
                                IconConfig.getInstance().getDirectionOfFlowLayerIconName()),
                        false);
            }
            directionOfFlowLayerMenuItem.setEnabled(true);
        }

        if (enabledDayaLayers.contains(DataLayer.TURN_RESTRICTION)) {
            if (turnRestrictionLayerLayerMenuItem == null) {
                turnRestrictionLayerLayerMenuItem = MainMenu.add(MainApplication.getMenu().imageryMenu,
                        new LayerActivator(DataLayer.TURN_RESTRICTION,
                                TurnRestrictionGuiConfig.getInstance().getLayerName(),
                                IconConfig.getInstance().getTurnRestrictonLayerIconName()),
                        false);
            }
            turnRestrictionLayerLayerMenuItem.setEnabled(true);
        }
    }


    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof ImproveOsmLayer) {
            if (event.getAddedLayer() instanceof MissingGeometryLayer) {
                PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(true);
            } else if (event.getAddedLayer() instanceof DirectionOfFlowLayer) {
                PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(true);
            } else if (event.getAddedLayer() instanceof TurnRestrictionLayer) {
                PreferenceManager.getInstance().saveTurnRestrictionLayerOpenedFlag(true);
            }
            zoomChanged();
        }
    }

    @Override
    public void layerOrderChanged(final LayerOrderChangeEvent event) {
        final Layer oldLayer = MainApplication.getLayerManager().getLayers().size() > 1
                ? MainApplication.getLayerManager().getLayers().get(1) : null;
        final Layer newLayer = MainApplication.getLayerManager().getActiveLayer();
        if (oldLayer != null && newLayer instanceof AbstractLayer) {
            if (oldLayer instanceof MissingGeometryLayer) {
                updateSelectedData(missingGeometryLayer, null);
            } else if (oldLayer instanceof DirectionOfFlowLayer) {
                updateSelectedData(directionOfFlowLayer, null);
            } else if (oldLayer instanceof TurnRestrictionLayer) {
                updateSelectedData(turnRestrictionLayer, null);
            }
        }
    }

    @Override
    public void layerRemoving(final LayerRemoveEvent event) {
        if (event.getRemovedLayer() instanceof ImproveOsmLayer) {
            final ImproveOsmLayer<?> removedLayer = (ImproveOsmLayer<?>) event.getRemovedLayer();
            if (removedLayer.hasSelectedItems()) {
                removedLayer.updateSelectedItem(null);
                updateDialog(null, null);
            }
        }

        if (event.getRemovedLayer() instanceof MissingGeometryLayer) {
            missingGeometryLayer = null;
        } else if (event.getRemovedLayer() instanceof DirectionOfFlowLayer) {
            directionOfFlowLayer = null;
        } else if (event.getRemovedLayer() instanceof TurnRestrictionLayer) {
            turnRestrictionLayer = null;
        }
        if (missingGeometryLayer == null && directionOfFlowLayer == null && turnRestrictionLayer == null) {
            // remove listeners
            PreferenceManager.getInstance().saveErrorSuppressFlag(false);
            MainApplication.getLayerManager().removeLayerChangeListener(this);
            NavigatableComponent.removeZoomChangeListener(this);
            org.openstreetmap.josm.spi.preferences.Config.getPref().removePreferenceChangeListener(this);
            if (MainApplication.getMap() != null) {
                MainApplication.getMap().mapView.removeMouseListener(this);
                listenersRegistered = false;
            }
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
                ThreadPool.getInstance().execute(new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer));
            }
            if (directionOfFlowLayer != null && directionOfFlowLayer.isVisible()) {
                if (MainApplication.getLayerManager().getActiveLayer() == directionOfFlowLayer) {
                    new InfoDialog()
                            .displayDirectionOfFlowEditTip(Util.zoom(MainApplication.getMap().mapView.getRealBounds()));
                }
                ThreadPool.getInstance().execute(new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer));
            }
            if (turnRestrictionLayer != null && turnRestrictionLayer.isVisible()) {
                ThreadPool.getInstance().execute(new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer));
            }

            new InfoDialog().displayLocationButtonTip();
        }
    }


    /* PreferenceChangeListener method */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            if (PreferenceManager.getInstance().missingGeometryDataPreferencesChanged(event.getKey(),
                    event.getNewValue().getValue().toString())) {
                ThreadPool.getInstance().execute(new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer));
            } else if (PreferenceManager.getInstance().directionOfFlowDataPreferencesChanged(event.getKey(),
                    event.getNewValue().getValue().toString())) {
                ThreadPool.getInstance().execute(new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer));
            } else if (PreferenceManager.getInstance().turnRestrictionDataPreferencesChanged(event.getKey(),
                    event.getNewValue().getValue().toString())) {
                ThreadPool.getInstance().execute(new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer));
            } else if (PreferenceManager.getInstance().isPanelIconVisibilityKey(event.getKey())) {
                PreferenceManager.getInstance().savePanelOpenedFlag(event.getNewValue().toString());
            }
        }
    }


    /* MouseListener implementation */

    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
            final Point point = event.getPoint();
            final boolean multiSelect = event.isShiftDown();
            if (Util.zoom(MainApplication.getMap().mapView.getRealBounds()) > Config.getInstance()
                    .getMaxClusterZoom()) {
                if (activeLayer instanceof MissingGeometryLayer) {
                    // select tiles
                    selectItem(missingGeometryLayer, point, multiSelect);
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    // select road segments
                    selectItem(directionOfFlowLayer, point, multiSelect);
                } else if (activeLayer instanceof TurnRestrictionLayer) {
                    selectTurnRestriction(point, multiSelect);
                }
            }
        }
    }

    private <T> void selectItem(final ImproveOsmLayer<T> layer, final Point point, final boolean multiSelect) {
        final T item = layer.nearbyItem(point, multiSelect);
        if (item != null) {
            if (!item.equals(layer.lastSelectedItem())) {
                updateSelectedData(layer, item);
            }
        } else {
            // clear selection
            updateSelectedData(layer, null);
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
                    if ((lastSelectedItem == null) || (lastSelectedItem.getTurnRestrictions() == null)) {
                        shouldSelect = true;
                    }
                }
            } else {
                shouldSelect = true;
            }
        }
        if (shouldSelect) {
            updateSelectedData(turnRestrictionLayer, turnRestriction);
        } else if (!multiSelect) {
            // clear selection
            updateSelectedData(turnRestrictionLayer, null);
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
        if (SwingUtilities.isLeftMouseButton(event) && (activeLayer instanceof ImproveOsmLayer) && Util
                .zoom(MainApplication.getMap().mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()) {
            startDrag = new Point(event.getX(), event.getY());
            endDrag = startDrag;
            MainApplication.getMap().mapView.addTemporaryLayer(itemSelectionLayer);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
        if ((activeLayer instanceof ImproveOsmLayer) && SwingUtilities.isLeftMouseButton(event) && Util
                .zoom(MainApplication.getMap().mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()) {
            MainApplication.getMap().mapView.removeTemporaryLayer(itemSelectionLayer);
            if (startDrag != null && endDrag != null && !startDrag.equals(endDrag)) {
                final LatLon startDragCoord = Util.pointToLatLon(startDrag);
                final LatLon endDragCoord = Util.pointToLatLon(endDrag);
                final ImproveOsmLayer<?> layer = ((ImproveOsmLayer<?>) activeLayer);
                SwingUtilities.invokeLater(() -> {
                    final Rectangle2D boundingBox = Util.buildRectangleFromCoordinates(startDragCoord.getX(),
                            startDragCoord.getY(), endDragCoord.getX(), endDragCoord.getY());
                    layer.updateSelectedItems(boundingBox, event.isShiftDown());
                    layer.invalidate();
                    MainApplication.getMap().mapView.repaint();

                    final LatLon location = layer.getSelectedItems().size() >= 1
                            ? new LatLon(boundingBox.getCenterX(), boundingBox.getCenterY()) : null;
                    updateDialog(layer.lastSelectedItem(), location);
                });
            }
        }
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
    public void selectSimpleTurnRestriction(final TurnRestriction turnRestriction) {
        turnRestrictionLayer.updateSelectedItem(null);
        updateSelectedData(turnRestrictionLayer, turnRestriction);
    }


    /* CommentObserver method */

    @Override
    public synchronized void createComment(final Comment comment) {
        final List<Layer> layers = MainApplication.getLayerManager().getLayers();
        ThreadPool.getInstance().execute(() -> {
            if (layers.contains(missingGeometryLayer) && missingGeometryLayer.hasSelectedItems()) {
                createComment(ServiceHandler.getMissingGeometryHandler(), missingGeometryLayer,
                        new MissingGeometryUpdateThread(detailsDialog, missingGeometryLayer), comment);
            } else if (layers.contains(directionOfFlowLayer) && directionOfFlowLayer.hasSelectedItems()) {
                createComment(ServiceHandler.getDirectionOfFlowHandler(), directionOfFlowLayer,
                        new DirectionOfFlowUpdateThread(detailsDialog, directionOfFlowLayer), comment);
            } else if (layers.contains(turnRestrictionLayer) && turnRestrictionLayer.hasSelectedItems()) {
                createComment(ServiceHandler.getTurnRestrictionHandler(), turnRestrictionLayer,
                        new TurnRestrictionUpdateThread(detailsDialog, turnRestrictionLayer), comment);
            }
        });
    }

    private <T> void createComment(final ServiceHandler<T> handler, final ImproveOsmLayer<T> layer,
            final UpdateThread<T> updateThread, final Comment comment) {
        PreferenceManager.getInstance().saveLastComment(layer, comment.getText());
        final List<T> items = layer.getSelectedItems();
        handler.comment(comment, items);

        if (comment.getStatus() != null) {
            // status changed - refresh data (possible to select only 1 status from filters)
            if (MainApplication.getLayerManager().getActiveLayer().equals(layer)) {
                SwingUtilities.invokeLater(() -> {
                    layer.updateSelectedItem(null);
                    updateDialog(null, getItemLocation(null));
                });
            }
            ThreadPool.getInstance().execute(updateThread);
        } else {
            // new comment added
            if (items.equals(layer.getSelectedItems())) {
                final T item = items.get(items.size() - 1);
                if (layer.getSelectedItems().size() == 1) {
                    updateDialog(item, getItemLocation(item));
                }
            }
        }
    }


    /* commonly used private methods and classes */

    private <T> LatLon getItemLocation(final T item) {
        LatLon coordinate = null;
        if (item instanceof Tile) {
            final Tile tile = (Tile) item;
            coordinate = tile.getPoints().get(0);
        } else if (item instanceof RoadSegment) {
            final RoadSegment roadSegment = (RoadSegment) item;
            coordinate = roadSegment.getPoints().get(0);
        } else if (item instanceof TurnRestriction) {
            final TurnRestriction turnRestriction = (TurnRestriction) item;
            coordinate = turnRestriction.getPoint() != null ? turnRestriction.getPoint()
                    : (turnRestriction.getTurnRestrictions() != null
                            ? turnRestriction.getTurnRestrictions().get(0).getPoint() : null);
        }
        return coordinate;
    }

    private <T> void updateSelectedData(final ImproveOsmLayer<T> layer, final T item) {
        SwingUtilities.invokeLater(() -> {
            updateLayerWithTheSelectedItem(layer, item);
            updateDialog(item, getItemLocation(item));
        });
    }

    private <T> void updateLayerWithTheSelectedItem(final ImproveOsmLayer<T> layer, final T item) {
        layer.updateSelectedItem(item);
        layer.invalidate();
        MainApplication.getMap().mapView.repaint();
    }

    private <T> void updateDialog(final T item, final LatLon itemsLocation) {
        List<Comment> comments = null;
        Status status = null;
        Integer noOfSelectedItems = 0;
        if (item instanceof Tile) {
            final Tile tile = (Tile) item;
            noOfSelectedItems = missingGeometryLayer.getSelectedItems().size();
            comments =
                    noOfSelectedItems == 1 ? ServiceHandler.getMissingGeometryHandler().retrieveComments(tile) : null;
            status = tile.getStatus();
        } else if (item instanceof RoadSegment) {
            final RoadSegment roadSegment = (RoadSegment) item;
            noOfSelectedItems = directionOfFlowLayer.getSelectedItems().size();
            comments = noOfSelectedItems == 1 ? ServiceHandler.getDirectionOfFlowHandler().retrieveComments(roadSegment)
                    : null;
            status = roadSegment.getStatus();
        } else if (item instanceof TurnRestriction) {
            final TurnRestriction turn = (TurnRestriction) item;
            noOfSelectedItems = turnRestrictionLayer.getSelectedItems().size();
            comments = noOfSelectedItems == 1 && turn.getTurnRestrictions() == null
                    ? ServiceHandler.getTurnRestrictionHandler().retrieveComments(turn) : null;
            status = turn.getStatus();
        }

        detailsDialog.updateUI(item, comments, itemsLocation, status, noOfSelectedItems);
    }


    private final class CopyAction extends AbstractAction {

        private static final long serialVersionUID = -6108419035272335873L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getActionCommand().equals(GuiConfig.getInstance().getLblCopy())) {
                final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
                String selection = "";
                if (activeLayer instanceof MissingGeometryLayer) {
                    if (missingGeometryLayer.hasSelectedItems()) {
                        selection = missingGeometryLayer.getSelectedItems().toString();
                    }
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    if (directionOfFlowLayer.hasSelectedItems()) {
                        selection = directionOfFlowLayer.getSelectedItems().toString();
                    }
                } else if (activeLayer instanceof TurnRestrictionLayer && turnRestrictionLayer.hasSelectedItems()) {
                    selection = turnRestrictionLayer.getSelectedItems().toString();
                }
                ClipboardUtils.copyString(selection);
            }
        }
    }


    private final class LayerActivator extends JosmAction {

        private static final long serialVersionUID = 383609516179512054L;
        private final DataLayer dataLayer;

        private LayerActivator(final DataLayer dataLayer, final String layerName, final String iconName) {
            super(layerName, new ImageProvider(iconName), null, null, false, null, false);
            this.dataLayer = dataLayer;
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (!listenersRegistered) {
                registerListeners();
            }
            switch (dataLayer) {
                case MISSING_GEOMETRY:
                    if (missingGeometryLayer == null) {
                        // add layer
                        missingGeometryLayer = new MissingGeometryLayer();
                        MainApplication.getLayerManager().addLayer(missingGeometryLayer);
                        PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(true);
                    }
                    break;
                case DIRECTION_OF_FLOW:
                    if (directionOfFlowLayer == null) {
                        directionOfFlowLayer = new DirectionOfFlowLayer();
                        MainApplication.getLayerManager().addLayer(directionOfFlowLayer);
                        PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(true);
                    }
                    break;
                default:
                    // turn restriction
                    if (turnRestrictionLayer == null) {
                        turnRestrictionLayer = new TurnRestrictionLayer();
                        MainApplication.getLayerManager().addLayer(turnRestrictionLayer);
                        PreferenceManager.getInstance().saveTurnRestrictionLayerOpenedFlag(true);
                    }
                    break;
            }
        }
    }


    /**
     * Defines the functionality produced by the mouse dragging.
     */
    private final class LayerSelectionListener extends MouseMotionAdapter {

        @Override
        public void mouseDragged(final MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                endDrag = new Point(event.getX(), event.getY());
                ImproveOsmPlugin.this.itemSelectionLayer.invalidate();
            }
        }
    }


    /**
     * Defines a temporary layer used for selecting multiple items from the map view. The layer life cycle is determined
     * by the mouse pressed/released actions.
     */
    private final class TemporarySelectionLayer extends AbstractMapViewPaintable {

        @Override
        public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
            graphics.draw(Util.buildRectangleFromCoordinates(startDrag.getX(), startDrag.getY(), endDrag.getX(),
                    endDrag.getY()));
        }
    }

    @Override
    public void downloadWay() {
        final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
        SimplePrimitiveId primitiveId;
        if (activeLayer instanceof DirectionOfFlowLayer && directionOfFlowLayer.hasSelectedItems()) {
            final List<RoadSegment> selectedRoadSegments = directionOfFlowLayer.getSelectedItems();
            final List<Long> downloadedRoadsId = new ArrayList<>();
            for(RoadSegment segment : selectedRoadSegments ){
                final long wayId = segment.getWayId();
                if (!downloadedRoadsId.contains(wayId)) {
                    primitiveId = new SimplePrimitiveId(wayId, OsmPrimitiveType.WAY);
                    downloadedRoadsId.add(wayId);
                    DownloadWayTask downloadWayTask = new DownloadWayTask(primitiveId);
                    MainApplication.worker.submit(downloadWayTask);
                }
            }
            detailsDialog.disableDownloadButton();
        }
    }
    
    @Override
    public void activeOrEditLayerChanged(final ActiveLayerChangeEvent event) {
        final Layer activeLayer = MainApplication.getLayerManager().getActiveLayer();
        if (activeLayer instanceof MissingGeometryLayer) {
            final Tile item = missingGeometryLayer.lastSelectedItem();
            updateSelectedData(missingGeometryLayer, item);
        } else if (activeLayer instanceof DirectionOfFlowLayer) {
            final RoadSegment item = directionOfFlowLayer.lastSelectedItem();
            updateSelectedData(directionOfFlowLayer, item);
        } else if (activeLayer instanceof TurnRestrictionLayer) {
            final TurnRestriction item = turnRestrictionLayer.lastSelectedItem();
            updateSelectedData(turnRestrictionLayer, item);
        }
    }
}