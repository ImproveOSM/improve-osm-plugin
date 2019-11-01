/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.entity.Cluster;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import com.telenav.josm.common.entity.Status;


/**
 * Defines a generic service response.
 *
 * @author Beata
 * @version $Revision$
 */
public class BaseResponse {
    // setters are not required,since GSON sets the fields directly using reflection.

    private Status status;
    private List<Cluster> clusters;
    private List<Comment> comments;


    public Status getStatus() {
        return status;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<Comment> getComments() {
        return comments;
    }
}