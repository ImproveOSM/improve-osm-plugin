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
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.event.KeyEvent;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicButtonPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.FeedbackPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 * Defines the right side dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionDetailsDialog extends ImproveOsmDetailsDialog<TurnRestriction> {

    private static final long serialVersionUID = 1060483415436777908L;

    private static Shortcut shortcut = Shortcut.registerShortcut(TurnRestrictionGuiConfig.getInstance().getLayerName(),
            TurnRestrictionGuiConfig.getInstance().getLayerTlt(), KeyEvent.VK_F3, Shortcut.CTRL);


    /**
     * Builds a new {@code TurnRestrictionDetailsDialog} object.
     */
    public TurnRestrictionDetailsDialog() {
        super(TurnRestrictionGuiConfig.getInstance().getLayerName(),
                IconConfig.getInstance().getTurnRestrictonShortcutName(),
                TurnRestrictionGuiConfig.getInstance().getLayerTlt(), shortcut);
    }


    @Override
    public BasicPanel<TurnRestriction> createInfoPanel() {
        return new InfoPanel();
    }

    @Override
    public BasicButtonPanel<TurnRestriction> createButtonPanel() {
        return new ButtonPanel();
    }

    @Override
    public FeedbackPanel createFeedbackPanel() {
        return new FeedbackPanel(Config.getTurnRestrictionInstance().getFeedbackUrl());
    }
}