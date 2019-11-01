/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;


/**
 * Displays the edit dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class DisplayEditDialogAction extends JosmAction {

    private static final long serialVersionUID = 7465727160123599818L;
    private final Status newStatus;
    private Status currentStatus;
    private final EditDialog dialog;


    /**
     * Builds a new object with the given arguments.
     *
     * @param title the title to be displayed in the dialog window
     * @param shortcutKey the shortcut key associated with this
     * @param newStatus the new {@code Status} to be applied
     * @param icon the {@code ImageIcon} of the dialog window
     */
    public DisplayEditDialogAction(final String title, final String shortcutKey, final Status newStatus,
            final ImageIcon icon) {
        super(shortcutKey, null, shortcutKey, ShortcutFactory.getInstance().getShortcut(shortcutKey), true);
        dialog = new EditDialog(newStatus, title, icon.getImage());
        this.newStatus = newStatus;
    }

    /**
     * Registers the comment observer to the corresponding components.
     *
     * @param commentObserver a {@code CommentObserver}
     */
    public void registerCommentObserver(final CommentObserver commentObserver) {
        dialog.registerCommentObserver(commentObserver);
    }

    /**
     * Sets the currently selected item(s) status.
     *
     * @param currentStatus a {@code Status} object
     */
    public void setCurrentStatus(final Status currentStatus) {
        this.currentStatus = currentStatus;
        dialog.setCurrentStatus(currentStatus);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (newStatus == null || ((currentStatus == Status.OPEN && newStatus != Status.OPEN)
                || (currentStatus != Status.OPEN && newStatus == Status.OPEN))) {
            dialog.setVisible(true);
        }
    }
}