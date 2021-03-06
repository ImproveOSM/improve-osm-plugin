/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolTip;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import com.telenav.josm.common.gui.ScrollableToolTip;


/**
 * Defines a list for displaying {@code Comment}s.
 *
 * @author Beata
 * @version $Revision$
 */
class CommentsList extends JList<Comment> {

    private static final long serialVersionUID = 4213303203872460926L;

    /**
     * Displays the given comments in a list.
     *
     * @param comments a {@code Comment} array
     */
    CommentsList(final Comment[] comments) {
        super(comments);
        setCellRenderer(new CommentCellRenderer());
        addListSelectionListener(new SelectionListener());
    }

    @Override
    public JToolTip createToolTip() {
        final ScrollableToolTip tip = new ScrollableToolTip(this);
        tip.setComponent(this);
        if (!getToolTipText().isEmpty()) {
            tip.setVisible(true);
        }
        return tip;
    }

    private final class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                final Comment selectedObj = getSelectedValue();
                if (selectedObj.getText() != null && selectedObj.getText().length() > Config.getInstance()
                        .getCommentDisplayLength()) {
                    setToolTipText(selectedObj.getText().substring(0, Config.getInstance().getCommentMaxLength()));
                } else {
                    setToolTipText(null);
                }
            }
        }
    }


    private class CommentCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -7149120070205196547L;

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
                final boolean isSelected, final boolean cellHasFocus) {
            final JLabel label =
                    (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final Comment comment = (Comment) value;
            label.setText(Formatter.formatComment(comment));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setToolTipText(null);
            return label;
        }
    }
}