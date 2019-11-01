/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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

    static final GridBagConstraints LBL_LOCATION = new GridBagConstraints(0, 0, 4, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints BTN_IMPROVE_OSM = new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints BTN_CUSTOM = new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.LINE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints TXT_CUSTOM = new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints BTN_COPY_LOC = new GridBagConstraints(0, 3, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);


    private Constraints() {}
}