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

import java.awt.event.KeyEvent;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicButtonPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.FeedbackPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 * Defines the right side dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryDetailsDialog extends ImproveOsmDetailsDialog<Tile> {

    private static final long serialVersionUID = -3541499221974100034L;

    private static Shortcut shortcut = Shortcut.registerShortcut(MissingGeometryGuiConfig.getInstance().getLayerName(),
            MissingGeometryGuiConfig.getInstance().getLayerTlt(), KeyEvent.VK_F3, Shortcut.CTRL);

    /**
     * Builds a new {@code MissingGeometryDetailsDialog} object.
     */
    public MissingGeometryDetailsDialog() {
        super(MissingGeometryGuiConfig.getInstance().getLayerName(),
                IconConfig.getInstance().getMissingGeometryShortcutName(),
                MissingGeometryGuiConfig.getInstance().getLayerTlt(), shortcut);
    }


    @Override
    public BasicPanel<Tile> createInfoPanel() {
        return new TilePanel();
    }

    @Override
    public BasicButtonPanel<Tile> createButtonPanel() {
        return new ButtonPanel();
    }

    @Override
    public FeedbackPanel createFeedbackPanel() {
        return new FeedbackPanel(Config.getMissingGeometryInstance().getFeedbackUrl());
    }
}