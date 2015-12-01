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
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.tools.ImageProvider;


/**
 * Utility class, holds icons and icon paths.
 *
 * @author Beata
 * @version $Revision$
 */
public final class IconConfig extends BaseConfig {

    private static final IconConfig INSTANCE = new IconConfig();

    private static final String CONFIG_FILE = "improveosm_icon.properties";

    private final String pluginIconName;

    /* DirectionOfFlow layer icons */
    private final String directionOfFlowShortcutName;
    private final Icon directionOfFlowlayerIcon;

    /* MissingGeometry layer icons */
    private final String missingGeometryShortcutName;
    private final Icon missingGeometryLayerIcon;

    /* button panel icons */
    private final ImageIcon filterIcon;
    private final ImageIcon locationIcon;
    private final ImageIcon commentIcon;
    private final ImageIcon solvedIcon;
    private final ImageIcon openIcon;
    private final ImageIcon invalidIcon;

    private final URL tipIconPath;

    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        directionOfFlowShortcutName = readProperty("directionOfFlow.dialog.shortcut");
        directionOfFlowlayerIcon = ImageProvider.get(readProperty("directionOfFlow.layer.icon"));
        missingGeometryShortcutName = readProperty("missingGeo.dialog.shortcut");
        missingGeometryLayerIcon = ImageProvider.get(readProperty("missingGeo.layer.icon"));

        filterIcon = ImageProvider.get(readProperty("filter.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));
        commentIcon = ImageProvider.get(readProperty("comment.icon"));
        solvedIcon = ImageProvider.get(readProperty("solved.icon"));
        openIcon = ImageProvider.get(readProperty("open.icon"));
        invalidIcon = ImageProvider.get(readProperty("invalid.icon"));

        tipIconPath = getClass().getResource(readProperty("tip.icon"));
    }

    public static IconConfig getInstance() {
        return INSTANCE;
    }

    public String getPluginIconName() {
        return pluginIconName;
    }

    public String getDirectionOfFlowShortcutName() {
        return directionOfFlowShortcutName;
    }

    public Icon getDirectionOfFlowlayerIcon() {
        return directionOfFlowlayerIcon;
    }

    public String getMissingGeometryShortcutName() {
        return missingGeometryShortcutName;
    }

    public Icon getMissingGeometryLayerIcon() {
        return missingGeometryLayerIcon;
    }

    public ImageIcon getFilterIcon() {
        return filterIcon;
    }

    public ImageIcon getLocationIcon() {
        return locationIcon;
    }

    public ImageIcon getCommentIcon() {
        return commentIcon;
    }

    public ImageIcon getSolvedIcon() {
        return solvedIcon;
    }

    public ImageIcon getOpenIcon() {
        return openIcon;
    }

    public ImageIcon getInvalidIcon() {
        return invalidIcon;
    }

    public URL getTipIconPath() {
        return tipIconPath;
    }
}