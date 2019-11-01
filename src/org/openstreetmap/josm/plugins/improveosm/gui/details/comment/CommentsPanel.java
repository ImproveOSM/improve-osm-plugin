/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JScrollPane;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.ContainerBuilder;


/**
 * Defines a panel displaying a list of {@code Comment}s
 *
 * @author Beata
 * @version $Revision$
 */
public class CommentsPanel extends BasicInfoPanel<List<Comment>> {

    private static final long serialVersionUID = -1002962542315503824L;

    private static final Dimension DIM = new Dimension(150, 100);


    public CommentsPanel() {
        setName(GuiConfig.getInstance().getPnlHistoryTitle());
    }


    @Override
    public void createComponents(final List<Comment> comments) {
        setLayout(new BorderLayout());
        final CommentsList commentsList = new CommentsList(comments.toArray(new Comment[0]));
        final JScrollPane cmp =
                ContainerBuilder.buildScrollPane(commentsList, GuiConfig.getInstance().getPnlHistoryTitle(), 100, DIM);
        add(cmp, BorderLayout.CENTER);
    }
}