/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import com.telenav.josm.common.gui.builder.MenuBuilder;


/**
 * Builds a new pop-up menu for the edit actions (solve, invalidate and reopen).
 *
 * @author Beata
 * @version $Revision$
 */
public class EditPopupMenu extends JPopupMenu {

    private static final long serialVersionUID = -8338945124356603087L;
    private final JMenuItem editMenuItem;
    private final JMenuItem commentMenuItem;


    /**
     * Builds a new pop-up menu with the given arguments.
     *
     * @param titleEdit the title of the edit action
     * @param titleEditComment the title of the edit and comment action
     * @param editShortcutKey the edit shortcut key
     * @param editCommentShortcutKey the edit and comment shortcut key
     * @param newStatus the new {@code Status} that is applied when the action is executed
     * @param editCommentIcon the {@code ImageIcon} that represents the edit and comment action
     */
    public EditPopupMenu(final String titleEdit, final String titleEditComment, final String editShortcutKey,
            final String editCommentShortcutKey, final Status newStatus, final ImageIcon editCommentIcon) {
        editMenuItem = MenuBuilder.build(new ChangeStatusAction(editShortcutKey, newStatus), titleEdit);
        commentMenuItem = MenuBuilder.build(
                new DisplayEditDialogAction(titleEditComment, editCommentShortcutKey, newStatus, editCommentIcon),
                titleEditComment);
        add(editMenuItem);
        add(commentMenuItem);
    }


    /**
     * Registers the comment observer to the corresponding components.
     *
     * @param commentObserver a {@code CommentObserver} object
     */
    public void registerCommentObserver(final CommentObserver commentObserver) {
        ((ChangeStatusAction) editMenuItem.getAction()).registerObserver(commentObserver);
        ((DisplayEditDialogAction) commentMenuItem.getAction()).registerCommentObserver(commentObserver);
    }

    /**
     * The status of the currently selected element(s).
     *
     * @param currentStatus a {@code Status} object
     */
    public void setCurrentStatus(final Status currentStatus) {
        ((ChangeStatusAction) editMenuItem.getAction()).setCurrentStatus(currentStatus);
        ((DisplayEditDialogAction) commentMenuItem.getAction()).setCurrentStatus(currentStatus);
    }


    /**
     * Changes the status of the currently selected item(s).
     *
     * @author beataj
     * @version $Revision$
     */
    private final class ChangeStatusAction extends BasicCommentAction {

        private static final long serialVersionUID = -2668866604147089526L;

        private ChangeStatusAction(final String shortcutKey, final Status status) {
            super(shortcutKey, status);
        }

        private ChangeStatusAction(final Status status) {
            super(status);
        }

        @Override
        String getCommentText() {
            return null;
        }

        @Override
        boolean isValid() {
            return true;
        }
    }
}