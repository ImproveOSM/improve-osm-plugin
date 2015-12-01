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
package org.openstreetmap.josm.plugins.improveosm.service.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.entity.Cluster;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class BaseResponse {
    // setters are not required,since GSON sets the fields directly using reflection.

    private ResponseStatus status;
    private List<Cluster> clusters;
    private List<Comment> comments;


    public ResponseStatus getStatus() {
        return status;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<Comment> getComments() {
        return comments;
    }
}