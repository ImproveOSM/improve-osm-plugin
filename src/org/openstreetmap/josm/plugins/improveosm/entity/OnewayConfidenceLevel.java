/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;


/**
 * Defines the confidence levels that can be applied to the TrafficFlowDirection layer.
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
}