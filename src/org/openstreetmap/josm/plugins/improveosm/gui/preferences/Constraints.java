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
package org.openstreetmap.josm.plugins.improveosm.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Defines {@code PreferencePanel} constraints.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constraints {

    static final GridBagConstraints LBL_LOCATION = new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(50, 0, 3, 0), 0, 0);

    static final GridBagConstraints BTN_IMPROVE_OSM = new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints BTN_CUSTOM = new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.LINE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints BTN_COPY_LOC = new GridBagConstraints(0, 3, 1, 1, 1, 1,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);


    private Constraints() {}
}