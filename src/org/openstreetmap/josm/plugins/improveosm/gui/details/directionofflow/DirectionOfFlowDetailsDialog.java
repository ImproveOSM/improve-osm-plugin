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

import java.awt.event.KeyEvent;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicButtonPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.FeedbackPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 * Defines the right side dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class DirectionOfFlowDetailsDialog extends ImproveOsmDetailsDialog<RoadSegment> {

    private static final long serialVersionUID = 6272681385708084893L;

    private static Shortcut shortcut = Shortcut.registerShortcut(DirectionOfFlowGuiConfig.getInstance().getLayerName(),
            DirectionOfFlowGuiConfig.getInstance().getLayerTlt(), KeyEvent.VK_F3, Shortcut.CTRL);

    /**
     * Builds a new direction of flow details dialog window.
     */
    public DirectionOfFlowDetailsDialog() {
        super(DirectionOfFlowGuiConfig.getInstance().getLayerName(),
                IconConfig.getInstance().getDirectionOfFlowShortcutName(),
                DirectionOfFlowGuiConfig.getInstance().getLayerTlt(), shortcut);
    }

    @Override
    public BasicPanel<RoadSegment> createInfoPanel() {
        return new InfoPanel();
    }

    @Override
    public BasicButtonPanel<RoadSegment> createButtonPanel() {
        return new ButtonPanel();
    }

    @Override
    public FeedbackPanel createFeedbackPanel() {
        return new FeedbackPanel(Config.getDirectionOfFlowInstance().getFeedbackUrl());
    }
}