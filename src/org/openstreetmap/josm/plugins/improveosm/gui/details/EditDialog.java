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

import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.BOLD_12;
import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.PLAIN_12;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObservable;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class EditDialog extends ModalDialog implements CommentObservable {

    private static final long serialVersionUID = 586082110516333909L;
    private static final Dimension DIM = new Dimension(300, 200);
    private static final Border BORDER = new EmptyBorder(5, 5, 2, 5);
    private CommentObserver observer;
    private final Status status;
    private JLabel lblError;
    private JTextArea txtComment;

    /**
     * Builds a new edit dialog with the given arguments.
     *
     * @param status a {@code Status) represents the new status to be added.
     * @param title the title of the dialog
     * @param icon the icon of the dialog
     */
    public EditDialog(final Status status, final String title, final Image icon) {
        super(title, icon, DIM);
        this.status = status;
        createComponents();
    }

    @Override
    protected void createComponents() {
        lblError = GuiBuilder.buildLabel(GuiConfig.getInstance().getTxtInvalidComment(), BOLD_12, Color.red, false);
        txtComment = GuiBuilder.buildTextArea(PreferenceManager.getInstance().loadLastComment(), PLAIN_12, Color.white);

        final JPanel pnlComment = new JPanel(new BorderLayout());
        pnlComment.setBorder(BORDER);
        pnlComment.add(GuiBuilder.buildScrollPane(txtComment, Color.white, true), BorderLayout.CENTER);
        pnlComment.setVerifyInputWhenFocusTarget(true);
        add(pnlComment, BorderLayout.CENTER);

        final JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pnlBtn.add(GuiBuilder.buildButton(new AddCommentAction(), GuiConfig.getInstance().getBtnOkLbl()));
        pnlBtn.add(GuiBuilder.buildButton(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl()));

        final JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.add(lblError, BorderLayout.LINE_START);
        pnlSouth.add(pnlBtn, BorderLayout.LINE_END);
        add(pnlSouth, BorderLayout.SOUTH);
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

    /* Handles the comment operation */
    private class AddCommentAction extends AbstractAction {

        private static final long serialVersionUID = -7862363740212492052L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final String text = txtComment.getText().trim();
            if (text.isEmpty()) {
                lblError.setVisible(true);
            } else {
                if (lblError.isVisible()) {
                    lblError.setVisible(false);
                }
                dispose();
                final String username = PreferenceManager.getInstance().loadOsmUsername();
                if (username.isEmpty()) {
                    final String newUsername =
                            JOptionPane.showInputDialog(Main.parent, GuiConfig.getInstance().getTxtMissingUsername(),
                                    GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE);
                    if (newUsername != null && !newUsername.isEmpty()) {
                        PreferenceManager.getInstance().saveOsmUsername(newUsername);
                        notifyObserver(new Comment(newUsername, null, text, status));
                    }
                } else {
                    notifyObserver(new Comment(username, null, text, status));
                }
            }
        }
    }
}