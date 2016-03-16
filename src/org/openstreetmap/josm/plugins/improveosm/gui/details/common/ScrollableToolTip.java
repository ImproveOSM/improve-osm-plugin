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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolTip;


/**
 * Defines a custom {@code JToolTip} that is scrollable. This tool tip can be used for displaying long text.
 *
 * @author Beata
 * @version $Revision$
 */
public class ScrollableToolTip extends JToolTip {

    private static final long serialVersionUID = 9106274298045932597L;

    private static final String PROPERTY_NAME = "tiptext";
    private static final Dimension DIM = new Dimension(350, 100);

    private final JComponent cmpParent;
    private final JTextArea txtToolTip;
    private final JScrollPane cmp;


    /**
     * Builds a new object.
     *
     * @param cmpParent the parent component
     */
    public ScrollableToolTip(final JComponent cmpParent) {
        this.cmpParent = cmpParent;
        if (this.cmpParent != null) {
            this.cmpParent.addMouseWheelListener(new ToolTipMouseWheelListener());
        }
        setPreferredSize(DIM);
        setLayout(new BorderLayout());
        txtToolTip = GuiBuilder.buildTextArea(null, getBackground(), false);
        cmp = GuiBuilder.buildScrollPane(txtToolTip, getBackground(), false);
        add(cmp, BorderLayout.CENTER);
    }

    @Override
    public void setTipText(final String tipText) {
        final String oldValue = this.txtToolTip.getText();
        txtToolTip.setText(tipText);
        txtToolTip.setCaretPosition(0);
        firePropertyChange(PROPERTY_NAME, oldValue, tipText);
    }


    private final class ToolTipMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(final MouseWheelEvent event) {
            cmp.dispatchEvent(event);
            cmpParent.dispatchEvent(
                    new MouseEvent(ScrollableToolTip.this, MouseEvent.MOUSE_MOVED, 0, 0, 0, 0, 0, false));
        }
    }
}