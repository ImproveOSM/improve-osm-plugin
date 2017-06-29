/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Displays information regarding the number of the selected items.
 *
 * @author ioanao
 * @version $Revision$
 */
public class SelectedItemsInfoPanel extends BasicInfoPanel<Integer> {

    private static final long serialVersionUID = 7149916568776209642L;

    private static final Integer MAX_LABEL_WIDTH = 1000;

    @Override
    protected void createComponents(final Integer numberOfItems) {
        add(LabelBuilder.build(numberOfItems + " " + GuiConfig.getInstance().getTxtNumberOfSelectedItems(),
                Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                new Rectangle(RECT_X, getPnlY(), MAX_LABEL_WIDTH, LINE_HEIGHT)));
    }
}