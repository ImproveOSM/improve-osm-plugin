/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObservable;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the common logic that is executed when a comment with status change is created.
 *
 * @author Beata
 * @version $Revision$
 */
abstract class BasicCommentAction extends JosmAction implements CommentObservable {

    private static final long serialVersionUID = 859855224700983694L;

    private final Status newStatus;
    private Status currentStatus;
    private CommentObserver observer;


    BasicCommentAction(final String shortcutKey, final Status newStatus) {
        super(shortcutKey, null, shortcutKey, ShortcutFactory.getInstance().getShortcut(shortcutKey), true);
        this.newStatus = newStatus;
    }

    BasicCommentAction(final Status newStatus) {
        this.newStatus = newStatus;
    }


    public void setCurrentStatus(final Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public void notifyObserver(final Comment comment) {
        if (observer != null) {
            observer.createComment(comment);
        }
    }

    @Override
    public void registerObserver(final CommentObserver observer) {
        this.observer = observer;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (newStatus == null || ((currentStatus == Status.OPEN && newStatus != Status.OPEN)
                || (currentStatus != Status.OPEN && newStatus == Status.OPEN))) {
            final String text = getCommentText();
            if (isValid()) {
                final String username = PreferenceManager.getInstance().loadOsmUsername();
                if (username.isEmpty()) {
                    final String newUsername =
                            JOptionPane.showInputDialog(MainApplication.getMainFrame(),
                                    GuiConfig.getInstance().getTxtMissingUsername(),
                                    GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE);
                    if (newUsername != null && !newUsername.isEmpty()) {
                        PreferenceManager.getInstance().saveOsmUsername(newUsername);
                        notifyObserver(new Comment(newUsername, null, text, newStatus));
                    }
                } else {
                    notifyObserver(new Comment(username, null, text, newStatus));
                }
            }
        }
    }

    abstract String getCommentText();

    abstract boolean isValid();
}