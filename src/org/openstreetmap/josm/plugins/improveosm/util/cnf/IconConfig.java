/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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
    private final Icon directionOfFlowLayerIcon;
    private final Icon missingGeometryLayerIcon;
    private final Icon turnRestrictonLayerIcon;

    private final String directionOfFlowLayerIconName;
    private final String missingGeometryLayerIconName;
    private final String turnRestrictonLayerIconName;

    private final String deleteIconName;

    /* button panel icons */
    private final ImageIcon filterIcon;
    private final ImageIcon commentIcon;
    private final ImageIcon solveIcon;
    private final ImageIcon reopenIcon;
    private final ImageIcon invalidIcon;
    private final ImageIcon locationIcon;
    private final ImageIcon downloadIcon;

    /* turn restriction icons */
    private final ImageIcon turnRestrictionIcon;
    private final ImageIcon selectedTurnRestrictionIcon;
    private final String turnRestrictionIconsPath;


    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        dialogShortcutName = readProperty("dialog.shortcut");
        directionOfFlowLayerIconName = readProperty("directionOfFlow.layer.icon");
        missingGeometryLayerIconName = readProperty("missingGeo.layer.icon");
        turnRestrictonLayerIconName = readProperty("turnRestriction.layer.icon");
        deleteIconName = readProperty("delete.icon");

        directionOfFlowLayerIcon = ImageProvider.get(directionOfFlowLayerIconName);
        missingGeometryLayerIcon = ImageProvider.get(missingGeometryLayerIconName);
        turnRestrictonLayerIcon = ImageProvider.get(turnRestrictonLayerIconName);

        filterIcon = ImageProvider.get(readProperty("filter.icon"));
        commentIcon = ImageProvider.get(readProperty("comment.icon"));
        solveIcon = ImageProvider.get(readProperty("solve.icon"));
        reopenIcon = ImageProvider.get(readProperty("open.icon"));
        invalidIcon = ImageProvider.get(readProperty("invalid.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));
        downloadIcon = ImageProvider.get(readProperty("download.icon"));

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

    public String getDirectionOfFlowLayerIconName() {
        return directionOfFlowLayerIconName;
    }

    public String getMissingGeometryLayerIconName() {
        return missingGeometryLayerIconName;
    }

    public String getTurnRestrictonLayerIconName() {
        return turnRestrictonLayerIconName;
    }

    public String getDeleteIconName() {
        return deleteIconName;
    }

    public Icon getDirectionOfFlowLayerIcon() {
        return directionOfFlowLayerIcon;
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
    
    public ImageIcon getDownloadIcon() {
        return downloadIcon;
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