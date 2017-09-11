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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.tools.GuiSizesHelper;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.observer.CommentObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.CancelAction;
import com.telenav.josm.common.gui.ModalDialog;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 * Dialog window that display a simple panel containing a text area where the user can introduce a text.
 *
 * @author Beata
 * @version $Revision$
 */
class EditDialog extends ModalDialog {

    private static final long serialVersionUID = 586082110516333909L;
    private static final Dimension DIM = new Dimension(300, 200);
    private static final Border BORDER = new EmptyBorder(5, 5, 2, 5);
    private final Status status;
    private JLabel lblError;
    private JTextArea txtComment;
    private JButton btnOk;


    /**
     * Builds a new edit dialog with the given arguments.
     *
     * @param status represents the new status to be added.
     * @param title the title of the dialog
     * @param icon the icon of the dialog
     */
    EditDialog(final Status status, final String title, final Image icon) {
        super(title, icon, GuiSizesHelper.getDimensionDpiAdjusted(DIM));
        setLocationRelativeTo(Main.map.mapView);
        this.status = status;
        createComponents();
    }


    @Override
    public void createComponents() {
        lblError = LabelBuilder.build(GuiConfig.getInstance().getTxtInvalidComment(), Font.BOLD, Color.red,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP, false);
        txtComment = TextComponentBuilder.buildTextArea(new EditDocument(),
                PreferenceManager.getInstance().loadLastComment(), Color.white, Font.PLAIN, true);

        final JPanel pnlComment = ContainerBuilder.buildBorderLayoutPanel(null,
                ContainerBuilder.buildScrollPane(txtComment, null, Color.white, Color.gray, 100, true, null), null,
                BORDER);
        pnlComment.setVerifyInputWhenFocusTarget(true);
        add(pnlComment, BorderLayout.CENTER);


        btnOk = ButtonBuilder.build(new AddCommentAction(status), GuiConfig.getInstance().getBtnOkLbl());
        final JPanel pnlBtn = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.TRAILING, btnOk,
                ButtonBuilder.build(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl()));
        final JPanel pnlSouth = ContainerBuilder.buildBorderLayoutPanel(lblError, pnlBtn, null);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    public void registerCommentObserver(final CommentObserver observer) {
        ((AddCommentAction) btnOk.getAction()).registerObserver(observer);
    }

    public void setCurrentStatus(final Status currentStatus) {
        ((AddCommentAction) btnOk.getAction()).setCurrentStatus(currentStatus);
    }

    private final class EditDocument extends PlainDocument {

        private static final long serialVersionUID = -6861902595242696120L;

        @Override
        public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
            if (str != null && txtComment.getText().length() <= Config.getInstance().getCommentMaxLength()) {
                super.insertString(offs, str, a);
            }
        }
    }


    /* Handles the comment operation */

    private final class AddCommentAction extends BasicCommentAction {

        private static final long serialVersionUID = -7862363740212492052L;

        private AddCommentAction(final Status status) {
            super(status);
        }

        @Override
        String getCommentText() {
            return txtComment.getText().trim();
        }

        @Override
        boolean isValid() {
            boolean result = true;
            final String text = getCommentText();
            if (text.isEmpty()) {
                lblError.setVisible(true);
                result = false;
            } else {
                if (lblError.isVisible()) {
                    lblError.setVisible(false);
                }
                dispose();
            }
            return result;
        }
    }
}