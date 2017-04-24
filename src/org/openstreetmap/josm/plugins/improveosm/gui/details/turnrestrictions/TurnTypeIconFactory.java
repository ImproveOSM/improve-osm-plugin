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