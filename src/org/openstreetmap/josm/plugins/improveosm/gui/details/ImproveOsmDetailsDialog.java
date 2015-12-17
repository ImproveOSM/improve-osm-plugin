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
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.tools.Shortcut;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class ImproveOsmDetailsDialog<T> extends ToggleDialog {

    private static final long serialVersionUID = 614130882392599446L;
    /** the preferred dimension of the panel components */
    private static final Dimension DIM = new Dimension(150, 100);
    private static final int DLG_HEIGHT = 50;
    private final BasicPanel<T> pnlInfo;
    private final CommentsPanel pnlComments;
    private final BasicButtonPanel<T> pnlBtn;


    /**
     * Builds a new direction of flow details dialog window.
     */
    public ImproveOsmDetailsDialog(final String layerName, final String shortcutName, final String tooltip,
            final Shortcut shortcut) {
        super(layerName, shortcutName, tooltip, shortcut, DLG_HEIGHT);

        /* build components */
        pnlInfo = createInfoPanel();
        final JScrollPane cmpInfo = GuiBuilder.buildScrollPane(pnlInfo.getName(), pnlInfo, getBackground(), DIM);
        pnlComments = new CommentsPanel();
        final FeedbackPanel pnlFeedback = createFeedbackPanel();
        final JTabbedPane pnlDetails = GuiBuilder.buildTabbedPane(cmpInfo, pnlComments, pnlFeedback);
        pnlBtn = createButtonPanel();
        final JPanel pnlMain = GuiBuilder.buildBorderLayoutPanel(pnlDetails, pnlBtn);
        add(pnlMain);
    }


    public abstract BasicPanel<T> createInfoPanel();

    public abstract BasicButtonPanel<T> createButtonPanel();

    public abstract FeedbackPanel createFeedbackPanel();

    /**
     * Registers the given observer to the corresponding components.
     *
     * @param commentObserver a {@code CommentObserver}
     */
    public void registerCommentObserver(final CommentObserver commentObserver) {
        pnlBtn.registerCommentObserver(commentObserver);
    }

    /**
     * Updates the UI with the given road segment.
     *
     * @param roadSegment a {@code RoadSegment}.
     */
    public void updateUI(final T item) {
        synchronized (this) {
            pnlInfo.updateData(item);
            pnlBtn.setItem(item);
            repaint();
        }
    }

    /**
     * Updates the UI with the given road segment and comment list.
     *
     * @param roadSegment a {@code RoadSegment}
     * @param comments a list of {@code Comment}s
     */
    public void updateUI(final T item, final List<Comment> comments) {
        synchronized (this) {
            pnlInfo.updateData(item);
            pnlComments.updateData(comments);
            pnlBtn.setItem(item);
            repaint();
        }
    }

    public boolean reloadComments() {
        return pnlComments.getComponents() == null || pnlComments.getComponents().length == 0;
    }

    public BasicPanel<T> getPnlInfo() {
        return pnlInfo;
    }
}