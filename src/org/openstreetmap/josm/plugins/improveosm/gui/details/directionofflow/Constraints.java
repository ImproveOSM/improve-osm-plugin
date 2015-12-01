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
package org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Holds constraints for the filter panel.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constraints {

    private Constraints() {}

    static final GridBagConstraints LBL_STATUS = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(7, 5, 3, 5), 0, 0);
    static final GridBagConstraints RB_OPEN = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints RB_SOLVED = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
    static final GridBagConstraints RB_INVALID = new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
    static final GridBagConstraints LBL_CONFIDENCE = new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static final GridBagConstraints CBB_C1 = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints CBB_C2 = new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
    static final GridBagConstraints CBB_C3 = new GridBagConstraints(3, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
}
