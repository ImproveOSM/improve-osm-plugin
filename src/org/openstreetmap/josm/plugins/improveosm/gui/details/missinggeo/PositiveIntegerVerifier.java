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
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;


/**
 * Defines an input verifier for integer text fields.
 *
 * @author Beata
 * @version $Revision$
 */
class PositiveIntegerVerifier extends InputVerifier implements KeyListener {

    private final JComponent component;
    private final String message;

    /**
     * Builds a new object based on the given arguments.
     *
     * @param component the {@code JComponent} that is validated
     * @param message a {@code String} to be displayed if the user input is invalid. This string is displayed as a
     * tool-tip.
     */
    PositiveIntegerVerifier(final JComponent component, final String message) {
        this.component = component;
        this.component.addKeyListener(this);
        this.message = message;
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            verify(component);
        } else {
            component.setBackground(Color.white);
            component.setToolTipText(null);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        // not supported
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // not supported
    }

    @Override
    public boolean verify(final JComponent component) {
        final String valueStr = ((JTextField) component).getText().trim();
        boolean valid;
        if (!validate(valueStr)) {
            component.setBackground(Color.pink);
            component.setToolTipText(message);
            valid = false;
        } else {
            component.setBackground(Color.white);
            component.setToolTipText(null);
            valid = true;
        }
        return valid;
    }

    boolean validate(final String value) {
        boolean valid = true;
        if (!value.isEmpty()) {
            try {
                final Integer intValue = Integer.parseInt(value);
                valid = intValue > 0;
            } catch (final NumberFormatException e) {
                valid = false;
            }
        }
        return valid;
    }
}