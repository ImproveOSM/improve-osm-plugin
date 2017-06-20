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
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.util.GuiSizesHelper;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.comment.CommentsPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.RoadSegmentInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.TileInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions.TurnRestrictionInfoPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Shortcut;
import com.telenav.josm.common.gui.builder.ContainerBuilder;


/**
 * Defines the common functionality of the ImeproveOsm details dialog windows.
 *
 * @author Beata
 * @version $Revision$
 */
public class ImproveOsmDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = 614130882392599446L;

    private static final Shortcut SHORTCUT = Shortcut.registerShortcut(GuiConfig.getInstance().getPluginName(),
            GuiConfig.getInstance().getPluginName(), KeyEvent.VK_F3, Shortcut.NONE);

    /** the preferred dimension of the panel components */
    private static final Dimension DIM = new Dimension(150, 100);
    private static final int DLG_HEIGHT = 50;
    private static final int SCROLL_BAR_UNIT = 100;

    /* dialog components */
    private final JScrollPane cmpInfo;
    private final TileInfoPanel pnlTileInfo;
    private final RoadSegmentInfoPanel pnlRoadSegmentInfo;
    private final TurnRestrictionInfoPanel pnlTurnRestrictionInfo;
    private final CommentsPanel pnlComments;
    private final ButtonPanel pnlBtn;


    /**
     * Builds a new direction of flow details dialog window.
     */
    public ImproveOsmDetailsDialog() {
        super(GuiConfig.getInstance().getDialogTitle(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginName(), SHORTCUT, DLG_HEIGHT, true, PreferenceEditor.class);

        /* build components */
        pnlTileInfo = new TileInfoPanel();
        pnlRoadSegmentInfo = new RoadSegmentInfoPanel();
        pnlTurnRestrictionInfo = new TurnRestrictionInfoPanel();

        cmpInfo = ContainerBuilder.buildScrollPane(ContainerBuilder.buildEmptyPanel(Color.WHITE),
                GuiConfig.getInstance().getPnlInfoTitle(), Color.white, null, SCROLL_BAR_UNIT, false, DIM);
        pnlComments = new CommentsPanel();
        final JTabbedPane pnlDetails = ContainerBuilder.buildTabbedPane(cmpInfo, pnlComments, new FeedbackPanel());
        pnlBtn = new ButtonPanel();
        final JPanel pnlMain = ContainerBuilder.buildBorderLayoutPanel(null, pnlDetails, pnlBtn, null);
        setPreferredSize(GuiSizesHelper.getDimensionDpiAdjusted(DIM));
        add(createLayout(pnlMain, false, null));
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
     * @param item the selected item
     * @param <T> the selected object
     * @param comments a list of {@code Comment}s
     */
    public <T> void updateUI(final T item, final List<Comment> comments) {
        synchronized (this) {
            pnlBtn.enablePanelActions(item);
            pnlComments.updateData(comments);
            updateUI(item);
        }
    }

    private <T> void updateUI(final T item) {
        synchronized (this) {
            pnlBtn.enablePanelActions(item);
            final Layer activeLayer = Util.getImproveOsmLayer();
            if (activeLayer == null) {
                // special case, all layers were removed and details panel's needs to be cleared
                pnlTileInfo.updateData(null);
                pnlRoadSegmentInfo.updateData(null);
                pnlTurnRestrictionInfo.updateData(null);
            } else {
                final Component cmpInfoView = cmpInfo.getViewport().getView();
                if (activeLayer instanceof MissingGeometryLayer) {
                    pnlTileInfo.updateData((Tile) item);
                    if (!(cmpInfoView instanceof TileInfoPanel)) {
                        cmpInfo.setViewportView(pnlTileInfo);
                    }
                } else if (activeLayer instanceof DirectionOfFlowLayer) {
                    pnlRoadSegmentInfo.updateData((RoadSegment) item);
                    if (!(cmpInfoView instanceof RoadSegmentInfoPanel)) {
                        cmpInfo.setViewportView(pnlRoadSegmentInfo);
                    }
                } else if (activeLayer instanceof TurnRestrictionLayer) {
                    pnlTurnRestrictionInfo.updateData((TurnRestriction) item);
                    if (!(cmpInfoView instanceof TurnRestrictionInfoPanel)) {
                        cmpInfo.setViewportView(pnlTurnRestrictionInfo);
                    }
                }
            }
            cmpInfo.revalidate();
            repaint();
        }
    }
}