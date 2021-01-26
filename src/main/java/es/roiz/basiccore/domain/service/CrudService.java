/*
 * Copyright 2019 Quini Roiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package es.roiz.basiccore.domain.service;

import es.roiz.basiccore.domain.dto.Dto;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface that contains the basic structure of an basic crud service. This layer connects the controller
 * layer with persistence layer.
 *
 * @param <DTO>  The Class with which we will work as data transfer object
 * @param <PK> The id Type of the Class
 */
public interface CrudService<DTO extends Dto, PK extends Serializable> {
    /**
     * Method create from the basic crud
     *
     * @param o Object (validated) to create and persist
     * @return returns the T object that has been saved
     */
    DTO create(DTO o) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

    /**
     * Method create from the basic crud
     *
     * @param collection Iterable<T> of objects (validated) to create and persist
     * @return returns the Iterable<T> objects that has been saved
     */
    Iterable<DTO> create(Iterable<DTO> collection) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method get from the basic crud (read)
     *
     * @param pk Primary Key from the object that we want to read
     * @return return the Optional type T object read
     */
    Optional<DTO> read(PK pk) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method get from the basic crud which list all T objects
     *
     * @return returns a Pageable List of T objects or inherited from it
     */
    Iterable<DTO> list() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method get from the basic crud which list the T objects correctly paginated
     *
     * @param from  Parameter from the position
     * @param limit Parameter that limits the number of results from the position
     * @return returns a Pageable T list or inherited from it correctly paginated
     */
    Page<? extends DTO> list(int from, int limit) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method get from the basic crud which list the T objects correctly paginated,
     * adding a simple filter. Every implementation of this method must override
     * the behavior of it.
     *
     * @param filter The filter with which we will filter the list result
     * @param from   Parameter from the position
     * @param limit  Parameter that limits the number of results from the position
     * @return returns a Pageable T list or inherited from it correctly paginated ant filtered
     */
    <S> Page<? extends DTO> list(int from, int limit, S filter) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException;

    /**
     * Method update from the basic CRUD
     *
     * @param o Object of type T that will be updated
     * @return returns the object correctly updated if don't have any problem.
     */
    DTO update(DTO o) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method delete from the basic CRUD
     *
     * @param pk The primary key of the object that will be deleted.
     */
    void delete(PK pk) throws SQLException;

    /**
     * Method delete from the basic CRUD
     *
     * @param o the object that will be deleted
     */
    void delete(DTO o) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method count, it's a basic method added to the basic CRUD to facilitate the basic operations.
     *
     * @return returns the number of this object instances on the database
     */
    Long count();

    /**
     * This utility method verifies that the object with the pk Primary key exists
     *
     * @param pk primary key of the object to search
     * @return returns a boolean with the existence of the object
     */
    boolean exists(PK pk);

    /**
     * Method to update a list of objects
     *
     * @param c A collection of T objects to update
     */
    void updateAll(Collection<DTO> c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * Method count that return the number of this object instances on the database, but applying a filter
     *
     * @param filter Filter with which the search will be done
     * @return returns the number of this object instances on the database applying the filter
     */
    <S> Long countFilter(S filter);
}
