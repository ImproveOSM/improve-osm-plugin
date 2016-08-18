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
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import com.telenav.josm.common.gui.ModalDialog;


/**
 * Defines a general model dialog, custom dialogs should extend this class.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class GeneralModalDialog extends ModalDialog {

    private static final long serialVersionUID = -1864531391713589510L;

    /**
     * Builds a new {@code ModalDialog} with the given arguments.
     *
     * @param title the dialog title
     * @param icon the icon to be displayed in the dialog header
     * @param size the size of the dialog
     */
    public GeneralModalDialog(final String title, final Image icon, final Dimension size) {
        super(title, icon, size);
        setLocationRelativeTo(Main.map.mapView);
        getRootPane().registerKeyboardAction(new EscListener(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private final class EscListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            GeneralModalDialog.this.dispatchEvent(new WindowEvent(GeneralModalDialog.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}