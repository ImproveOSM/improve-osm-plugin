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
package org.openstreetmap.josm.plugins.improveosm.entity;

import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public enum OnewayConfidenceLevel {

    /** the highest confidence level */
    C1 {

        @Override
        public String toString() {
            return DirectionOfFlowGuiConfig.getInstance().getLblC1();
        }
    },

    /** the middle confidence level */
    C2 {

        @Override
        public String toString() {
            return DirectionOfFlowGuiConfig.getInstance().getLblC2();
        }

    },

    /** the lowest confidence level */
    C3 {

        @Override
        public String toString() {
            return DirectionOfFlowGuiConfig.getInstance().getLblC3();
        }
    };

    /**
     * Returns the short display name.
     *
     * @return a string
     */
    public String shortDisplayName() {
        return toString();
    }

    /**
     * Returns the long display name.
     *
     * @return a string
     */
    public String longDisplayName() {
        return toString().trim() + " " + DirectionOfFlowGuiConfig.getInstance().getLblOneway();
    }
}