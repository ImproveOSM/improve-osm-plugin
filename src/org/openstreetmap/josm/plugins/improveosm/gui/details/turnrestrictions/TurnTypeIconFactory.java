/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;


/**
 * Factory for the turn restriction types icons.
 *
 * @author ioanao
 * @version $Revision$
 */
final class TurnTypeIconFactory {

    /* the icons extension */
    private static final String EXT = ".png";

    private static final TurnTypeIconFactory UNIQUE_INSTANCE = new TurnTypeIconFactory();

    private final ImageIcon defaultIcon = IconConfig.getInstance().getTurnRestrictionIcon();

    private final Map<String, ImageIcon> map = new LinkedHashMap<>();

    private TurnTypeIconFactory() {}


    static TurnTypeIconFactory getInstance() {
        return UNIQUE_INSTANCE;
    }


    /**
     * Returns the icon corresponding to the given type. The method returns a default icon, if no icon corresponds to
     * the given type.
     *
     * @param type specifies the turn restriction type
     * @return an {@code ImageIcon} object
     */
    ImageIcon getIcon(final String type) {
        ImageIcon icon = map.get(type);
        if (icon == null) {
            final IconConfig iconCnf = IconConfig.getInstance();
            try {
                icon = ImageProvider.get(iconCnf.getTurnRestrictionIconsPath() + type.toLowerCase() + EXT);
                map.put(type, icon);
            } catch (final RuntimeException e) {
                icon = defaultIcon;
            }
        }
        return icon;
    }
}