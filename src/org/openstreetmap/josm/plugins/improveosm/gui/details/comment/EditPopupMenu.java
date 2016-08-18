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

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class EditPopupMenu extends JPopupMenu {

    private static final long serialVersionUID = -8338945124356603087L;
    private final JMenuItem editMenuItem;
    private final JMenuItem commentMenuItem;


    public EditPopupMenu(final Status status, final String titleEdit, final String titleEditComment,
            final ImageIcon icon) {
        editMenuItem = buildMenuItem(new ChangeStatusAction(status), titleEdit);
        commentMenuItem =
                buildMenuItem(new DisplayEditDialogAction(status, titleEditComment, icon), titleEditComment);
        add(editMenuItem);
        add(commentMenuItem);
    }


    public void registerCommentObserver(final CommentObserver commentObserver) {
        ((ChangeStatusAction) editMenuItem.getAction()).registerObserver(commentObserver);
        ((DisplayEditDialogAction) commentMenuItem.getAction()).registerCommentObserver(commentObserver);
    }

    private JMenuItem buildMenuItem(final AbstractAction action, final String title) {
        final JMenuItem menuItem = new JMenuItem(action);
        menuItem.setText(title);
        return menuItem;
    }

    private final class ChangeStatusAction extends BasicCommentAction {

        private static final long serialVersionUID = -2668866604147089526L;

        public ChangeStatusAction(final Status status) {
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