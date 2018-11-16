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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.MainLayerManager.ActiveLayerChangeEvent;
import org.openstreetmap.josm.gui.widgets.DisableShortcutsOnFocusGainedTextField;
import org.openstreetmap.josm.tools.GuiSizesHelper;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.improveosm.gui.details.comment.CommentsPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.RoadSegmentInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.TileInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.DownloadWayObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import org.openstreetmap.josm.gui.layer.MainLayerManager;

/**
 * Defines the common functionality of the ImproveOsm details dialog windows.
 *
 * @author Beata
 * @version $Revision$
 */
public class ImproveOsmDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = 614130882392599446L;


    /** the preferred dimension of the panel components */
    private static final Dimension DIM = new Dimension(150, 100);
    private static final int DLG_HEIGHT = 50;
    private static final int SCROLL_BAR_UNIT = 100;

    /* dialog components */
    private final JScrollPane cmpInfo;
    private final TileInfoPanel pnlTileInfo;
    private final RoadSegmentInfoPanel pnlRoadSegmentInfo;
    private final TurnRestrictionInfoPanel pnlTurnRestrictionInfo;
    private final SelectedItemsInfoPanel noOfTilesInfo;
    private final CommentsPanel pnlComments;
    private final ButtonPanel pnlBtn;
    private final JTextField searchBox;


    /**
     * Builds a new direction of flow details dialog window.
     */
    public ImproveOsmDetailsDialog() {
        super(GuiConfig.getInstance().getDialogTitle(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginName(),
                ShortcutFactory.getInstance().getShortcut(GuiConfig.getInstance().getDialogShortcutTxt()), DLG_HEIGHT,
                true, PreferenceEditor.class);

        /* build components */
        pnlTileInfo = new TileInfoPanel();
        pnlRoadSegmentInfo = new RoadSegmentInfoPanel();
        pnlTurnRestrictionInfo = new TurnRestrictionInfoPanel();
        noOfTilesInfo = new SelectedItemsInfoPanel();
        cmpInfo = ContainerBuilder.buildScrollPane(ContainerBuilder.buildEmptyPanel(Color.WHITE),
                GuiConfig.getInstance().getPnlInfoTitle(), Color.white, null, SCROLL_BAR_UNIT, false, DIM);
        pnlComments = new CommentsPanel();
        final JTabbedPane pnlDetails = ContainerBuilder.buildTabbedPane(cmpInfo, pnlComments, new FeedbackPanel());
        searchBox = new DisableShortcutsOnFocusGainedTextField("latitude,longitude");
        searchBox.addActionListener(new SearchBoxListeners());
        pnlBtn = new ButtonPanel();
        final JPanel pnlOptions = ContainerBuilder.buildGridLayoutPanel(2, 1, searchBox, pnlBtn);
        final JPanel pnlMain = ContainerBuilder.buildBorderLayoutPanel(null, pnlDetails, pnlOptions, null);
        setPreferredSize(GuiSizesHelper.getDimensionDpiAdjusted(DIM));
        MainApplication.getLayerManager().addActiveLayerChangeListener(new ActiveLayerListener());
        add(createLayout(pnlMain, false, null));
    }
    
    public void addDownloadWayButtonObserver(final DownloadWayObserver observer) {
        pnlBtn.addObserver(observer);
    }
    
    /**
     * Registers the comment observer to the corresponding UI component.
     *
     * @param observer a {@code CommentObserver} object
     */
    public void registerCommentObserver(final CommentObserver observer) {
        pnlBtn.registerCommentObserver(observer);
    }

    /**
     * Registers the turn restriction selection observer to the corresponding UI component.
     *
     * @param observer a {@code TurnRestrictionSelectionObserver} object
     */
    public void registerTurnRestrictionSelectionObserver(final TurnRestrictionSelectionObserver observer) {
        pnlTurnRestrictionInfo.registerSelectionObserver(observer);
    }

    /**
     * Updates the UI with the given object's properties and comment list.
     *
     * @param <T> the selected object
     * @param lastSelectedItem the selected item
     * @param comments a list of {@code Comment}s
     * @param location the location of the selected item
     * @param status the status of the selected item
     * @param numberOfSelectedItems
     */
    public <T> void updateUI(final T lastSelectedItem, final List<Comment> comments, final LatLon location,
            final Status status, final Integer numberOfSelectedItems) {
        synchronized (this) {
            pnlComments.updateData(comments);
            pnlBtn.enablePanelActions(status, location);
            updateUI(lastSelectedItem, numberOfSelectedItems);
            pnlBtn.revalidate();
        }
    }

    private <T> void updateUI(final T lastSelectedItem, final Integer numberOfSelectedItems) {
        synchronized (this) {
            final Layer activeLayer = Util.getImproveOsmLayer();
            if (activeLayer == null) {
                // special case, all layers were removed and details panel's needs to be cleared
                pnlTileInfo.updateData(null);
                pnlRoadSegmentInfo.updateData(null);
                pnlTurnRestrictionInfo.updateData(null);
                noOfTilesInfo.updateData(null);
            } else {
                final Component cmpInfoView = cmpInfo.getViewport().getView();
                if (numberOfSelectedItems > 1) {
                    noOfTilesInfo.updateData(numberOfSelectedItems);
                    cmpInfo.setViewportView(noOfTilesInfo);
                } else {
                    if (activeLayer instanceof MissingGeometryLayer) {
                        pnlTileInfo.updateData((Tile) lastSelectedItem);
                        if (!(cmpInfoView instanceof TileInfoPanel)) {
                            cmpInfo.setViewportView(pnlTileInfo);
                        }
                    } else if (activeLayer instanceof DirectionOfFlowLayer) {
                        pnlRoadSegmentInfo.updateData((RoadSegment) lastSelectedItem);
                        if (!(cmpInfoView instanceof RoadSegmentInfoPanel)) {
                            cmpInfo.setViewportView(pnlRoadSegmentInfo);
                        }
                    } else if (activeLayer instanceof TurnRestrictionLayer) {
                        pnlTurnRestrictionInfo.updateData((TurnRestriction) lastSelectedItem);
                        if (!(cmpInfoView instanceof TurnRestrictionInfoPanel)) {
                            cmpInfo.setViewportView(pnlTurnRestrictionInfo);
                        }
                    }
                }
            }
            cmpInfo.revalidate();
            repaint();
        }
    }


    class SearchBoxListeners implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            final String latLonValue = searchBox.getText();
            if (latLonValue.split(",").length == 2) {
                try {
                    final double lat = Double.parseDouble(latLonValue.split(",")[0]);
                    final double lon = Double.parseDouble(latLonValue.split(",")[1]);
                    final LatLon searchedLocation = new LatLon(lat, lon);
                    if (searchedLocation.isValid()) {
                        final EastNorth demoZoomLocation =
                                searchedLocation.getEastNorth(MainApplication.getMap().mapView.getProjection());
                        MainApplication.getMap().mapView.zoomTo(demoZoomLocation, 1);
                    } else {
                        searchBox.setText("Incorrect values for latitude or longitude.");
                    }

                } catch (final NumberFormatException e1) {
                    searchBox.setText("Incorrect format for latitude or longitude.");
                }
            } else {
                searchBox.setText("Incorrect format.(expected: lat,lon)");
            }
            MainApplication.getMap().mapView.requestFocus();
        }
    }

    
    class ActiveLayerListener implements MainLayerManager.ActiveLayerChangeListener {

        @Override
        public void activeOrEditLayerChanged(ActiveLayerChangeEvent arg0) {
            pnlBtn.setDownloadButtonAccess();
            revalidate();
        }
    }
}