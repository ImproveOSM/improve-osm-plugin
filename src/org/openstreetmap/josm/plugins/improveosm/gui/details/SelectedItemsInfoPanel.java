/*
 *  Copyright 2017 Telenav, Inc.
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