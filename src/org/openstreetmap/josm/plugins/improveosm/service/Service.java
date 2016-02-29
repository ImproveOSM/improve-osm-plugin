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
 * Provides an interface to the back-end service. Each back-end service provides similar functionality.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> the object type served by the service
 */
public interface Service<T> {

    /**
     * Searches for items in the given area based on the given filters. The zoom level specifies the type of the
     * returned item.
     *
     * @param bbox a {@code BoundingBox} represents a search area
     * @param filter a {@code SearchFilter} represents the searching criteria
     * @param zoom represents the current zoom level
     * @return a {@code DataSet} containing either a list of clusters or items of type {@code T}
     * @throws ServiceException if the search operation fails
     */
    DataSet<T> search(BoundingBox bbox, SearchFilter filter, int zoom) throws ServiceException;

    /**
     * Retrieve the comments associated to the given entity.
     *
     * @param entity specifies the item for which the comments are searched
     * @return a list of {@code Comment}s
     * @throws ServiceException if the retrieve operation fails
     */
    List<Comment> retrieveComments(T entity) throws ServiceException;

    /***
     * Adds a comment to the list of entities. If the comment has a status, then the status of the items is also
     * changed.
     *
     * @param comment a {@code Comment} to be added
     * @param entities a list of object to be changed
     * @throws ServiceException if the comment operation fails
     */
    void comment(final Comment comment, final List<T> entities) throws ServiceException;
}