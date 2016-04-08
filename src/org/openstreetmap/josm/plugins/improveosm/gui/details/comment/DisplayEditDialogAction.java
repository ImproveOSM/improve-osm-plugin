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
package org.openstreetmap.josm.plugins.improveosm.gui.details.comment;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;


/**
 * Displays the edit dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class DisplayEditDialogAction extends AbstractAction {

    private static final long serialVersionUID = 7465727160123599818L;
    private final Status status;
    private final String title;
    private final ImageIcon icon;
    private CommentObserver commentObserver;


    public DisplayEditDialogAction(final Status status, final String title, final ImageIcon icon) {
        this.status = status;
        this.title = title;
        this.icon = icon;
    }


    public void registerCommentObserver(final CommentObserver commentObserver) {
        this.commentObserver = commentObserver;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final EditDialog dialog = new EditDialog(status, title, icon.getImage());
        dialog.registerCommentObserver(commentObserver);
        dialog.setVisible(true);
    }
}