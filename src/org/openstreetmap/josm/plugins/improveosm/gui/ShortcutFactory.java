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
package org.openstreetmap.josm.plugins.improveosm.gui;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 * Factory for the shortcuts associated with the plugin.
 *
 * @author beataj
 * @version $Revision$
 */
public class ShortcutFactory {

    private static final Map<String, Shortcut> defaultShortcutMap = initializeDefaultShortcuts();
    private final Map<String, Shortcut> shortcutMap;

    private static final ShortcutFactory INSTANCE = new ShortcutFactory();


    private static Map<String, Shortcut> initializeDefaultShortcuts() {
        final GuiConfig guiConfig = GuiConfig.getInstance();
        final Map<String, Shortcut> map = new HashMap<>();
        map.put(guiConfig.getDialogShortcutTxt(), Shortcut.registerShortcut(guiConfig.getDialogShortcutTxt(),
                guiConfig.getDialogShortcutTxt(), KeyEvent.VK_0, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getCommentShortcutTxt(), Shortcut.registerShortcut(guiConfig.getCommentShortcutTxt(),
                guiConfig.getCommentShortcutTxt(), KeyEvent.VK_1, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getSolveShortcutTxt(), Shortcut.registerShortcut(guiConfig.getSolveShortcutTxt(),
                guiConfig.getSolveShortcutTxt(), KeyEvent.VK_2, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getInvalidShortcutTxt(), Shortcut.registerShortcut(guiConfig.getInvalidShortcutTxt(),
                guiConfig.getInvalidShortcutTxt(), KeyEvent.VK_3, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getReopenShortcutTxt(), Shortcut.registerShortcut(guiConfig.getReopenShortcutTxt(),
                guiConfig.getReopenShortcutTxt(), KeyEvent.VK_4, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getSolveCommentShortcutTxt(),
                Shortcut.registerShortcut(guiConfig.getSolveCommentShortcutTxt(),
                        guiConfig.getSolveCommentShortcutTxt(), KeyEvent.VK_5, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getInvalidCommentShortcutTxt(),
                Shortcut.registerShortcut(guiConfig.getInvalidCommentShortcutTxt(),
                        guiConfig.getInvalidCommentShortcutTxt(), KeyEvent.VK_6, Shortcut.ALT_SHIFT));
        map.put(guiConfig.getReopenCommentShortcutTxt(),
                Shortcut.registerShortcut(guiConfig.getReopenCommentShortcutTxt(),
                        guiConfig.getReopenCommentShortcutTxt(), KeyEvent.VK_7, Shortcut.ALT_SHIFT));
        return map;
    }

    private ShortcutFactory() {
        shortcutMap = new HashMap<>();
        Shortcut.listAll().forEach(item -> {
            if (item.getShortText().startsWith(GuiConfig.getInstance().getPluginShortName())) {
                shortcutMap.put(item.getShortText(), item);
            }
        });
    }

    /**
     * Returns the unique instance of the shortcut factory.
     *
     * @return a {@code ShortcutFactory}
     */
    public static ShortcutFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the shortcut associated with the given key. The method returns the default shortcut associated with the
     * key, if the shortcut was not registered in JOSM.
     *
     * @param key a {@code String} representing a shortcut key
     * @return a {@code Shortcut}
     */
    public Shortcut getShortcut(final String key) {
        final Shortcut shortcut = shortcutMap.get(key);
        return shortcut != null ? shortcut : defaultShortcutMap.get(key);
    }
}