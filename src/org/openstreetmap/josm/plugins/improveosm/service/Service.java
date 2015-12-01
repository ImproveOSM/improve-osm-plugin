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
package org.openstreetmap.josm.plugins.improveosm.service;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public interface Service<T> {

    DataSet<T> search(BoundingBox bbox, SearchFilter filter, int zoom) throws ServiceException;

    List<Comment> retrieveComments(T entity) throws ServiceException;

    void comment(final Comment comment, final List<T> entities) throws ServiceException;
}