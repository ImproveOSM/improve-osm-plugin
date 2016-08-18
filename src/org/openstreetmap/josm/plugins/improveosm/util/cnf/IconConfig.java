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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.tools.ImageProvider;
import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Utility class, holds icons and icon paths.
 *
 * @author Beata
 * @version $Revision$
 */
public final class IconConfig extends BaseConfig {

    private static final IconConfig INSTANCE = new IconConfig();

    private static final String CONFIG_FILE = "improveosm_icon.properties";

    /* plugin icon */
    private final String pluginIconName;

    /* dialog shotrcut name */
    private final String dialogShortcutName;

    /* data layer icons */
    private final Icon directionOfFlowlayerIcon;
    private final Icon missingGeometryLayerIcon;
    private final Icon turnRestrictonLayerIcon;

    /* button panel icons */
    private final ImageIcon filterIcon;
    private final ImageIcon commentIcon;
    private final ImageIcon solveIcon;
    private final ImageIcon reopenIcon;
    private final ImageIcon invalidIcon;
    private final ImageIcon locationIcon;

    /* turn restriction icons */
    private final ImageIcon turnRestrictionIcon;
    private final ImageIcon selectedTurnRestrictionIcon;
    private final String turnRestrictionIconsPath;


    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        dialogShortcutName = readProperty("dialog.shortcut");
        directionOfFlowlayerIcon = ImageProvider.get(readProperty("directionOfFlow.layer.icon"));
        missingGeometryLayerIcon = ImageProvider.get(readProperty("missingGeo.layer.icon"));
        turnRestrictonLayerIcon = ImageProvider.get(readProperty("turnRestriction.layer.icon"));

        filterIcon = ImageProvider.get(readProperty("filter.icon"));
        commentIcon = ImageProvider.get(readProperty("comment.icon"));
        solveIcon = ImageProvider.get(readProperty("solve.icon"));
        reopenIcon = ImageProvider.get(readProperty("open.icon"));
        invalidIcon = ImageProvider.get(readProperty("invalid.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));

        turnRestrictionIcon = ImageProvider.get(readProperty("turnRestriction.icon"));
        selectedTurnRestrictionIcon = ImageProvider.get(readProperty("turnRestriction.sel.icon"));
        turnRestrictionIconsPath = readProperty("turnRestriction.types.icons.path");
    }


    public static IconConfig getInstance() {
        return INSTANCE;
    }


    public String getPluginIconName() {
        return pluginIconName;
    }

    public String getDialogShortcutName() {
        return dialogShortcutName;
    }

    public Icon getDirectionOfFlowlayerIcon() {
        return directionOfFlowlayerIcon;
    }

    public Icon getMissingGeometryLayerIcon() {
        return missingGeometryLayerIcon;
    }

    public Icon getTurnRestrictonLayerIcon() {
        return turnRestrictonLayerIcon;
    }

    public ImageIcon getFilterIcon() {
        return filterIcon;
    }

    public ImageIcon getCommentIcon() {
        return commentIcon;
    }

    public ImageIcon getSolveIcon() {
        return solveIcon;
    }

    public ImageIcon getReopenIcon() {
        return reopenIcon;
    }

    public ImageIcon getInvalidIcon() {
        return invalidIcon;
    }

    public ImageIcon getLocationIcon() {
        return locationIcon;
    }

    public ImageIcon getTurnRestrictionIcon() {
        return turnRestrictionIcon;
    }

    public ImageIcon getSelectedTurnRestrictionIcon() {
        return selectedTurnRestrictionIcon;
    }

    public String getTurnRestrictionIconsPath() {
        return turnRestrictionIconsPath;
    }

}