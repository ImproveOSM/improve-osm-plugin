/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Utility class, holds the {@code GridBagConstraints}s used for the filter UI.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constraints {

    static final GridBagConstraints LBL_STATUS = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(7, 5, 3, 5), 0, 0);
    static final GridBagConstraints RB_OPEN = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 3), 0, 0);
    static final GridBagConstraints RB_SOLVED = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 3), 0, 0);
    static final GridBagConstraints RB_INVALID = new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 3), 0, 0);
    static final GridBagConstraints LBL_TYPE = new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static final GridBagConstraints RB_ROAD = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 3), 0, 0);
    static final GridBagConstraints RB_PARKING = new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 3), 0, 0);
    static final GridBagConstraints RB_BOTH = new GridBagConstraints(3, 1, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 3), 0, 0);
    static final GridBagConstraints LBL_WATER = new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static final GridBagConstraints RB_WATER = new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 3), 0, 0);
    static final GridBagConstraints LBL_PEDESTRIAN = new GridBagConstraints(0, 3, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
    static final GridBagConstraints RB_PATH = new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 3), 0, 0);
    static final GridBagConstraints LBL_COUNT = new GridBagConstraints(0, 4, 1, 1, 1, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(3, 5, 3, 5), 0, 0);
    static final GridBagConstraints TXT_COUNT = new GridBagConstraints(1, 4, 1, 1, 1, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(3, 5, 3, 5), 0, 0);


    private Constraints() {}

}