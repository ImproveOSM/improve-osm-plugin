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
package org.openstreetmap.josm.plugins.improveosm;

import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.util.Util;

/**
 *
 * @author Beata
 * @version $Revision$
 */
abstract class UpdateThread<T> implements Runnable {

    @Override
    public void run() {
        if (Main.map != null && Main.map.mapView != null) {
            final BoundingBox bbox = new BoundingBox(Main.map.mapView);
            if (bbox != null) {
                final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
                final DataSet<T> result = searchData(bbox, zoom);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        updateUI(result);
                        Main.map.repaint();
                    }
                });

            }
        }
    }

    abstract DataSet<T> searchData(BoundingBox bbox, int zoom);

    abstract void updateUI(DataSet<T> result);
}