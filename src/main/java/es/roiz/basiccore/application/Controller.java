/*
 * Copyright 2020 Quini Roiz
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

package es.roiz.basiccore.application;

import es.roiz.basiccore.domain.dto.Dto;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * Interface that contains the basic structure of a CRUD es.roiz.recomendador.controller
 * and it's methods
 *
 * @param <DTO>  The Class with which we will work as data transfer object
 * @param <PK> The id Type of the Class
 */
public interface Controller<DTO extends Dto, PK extends Serializable> {
    /**
     * Method create from the basic crud
     *
     * @param o Object (validated) to create and persist
     * @return returns the T object that has been saved
     */
    ResponseEntity create(@Valid DTO o) throws InstantiationException, IllegalAccessException;

    /**
     * Method get from the basic crud (read)
     *
     * @param pk Primary Key from the object that we want to read
     * @return return the T object read
     */
    ResponseEntity get(PK pk) throws InstantiationException, IllegalAccessException;

    /**
     * Method get from the basic crud which list all T objects
     *
     * @return returns a List of T objects or inherited from it
     */
    <DTO> Iterable<DTO> get() throws InstantiationException, IllegalAccessException;

    /**
     * Method get from the basic crud which list the T objects correctly paginated
     *
     * @param from  Parameter from the position
     * @param limit Parameter that limits the number of results from the position
     * @return returns a T list or inherited from it correctly paginated
     */
    <DTO> List<? extends DTO> get(int from, int limit) throws InstantiationException, IllegalAccessException;

    /**
     * Method get from the basic crud which list the T objects correctly paginated,
     * adding a simple filter. Every implementation of this method must override
     * the behavior of it.
     *
     * @param filter The filter with which we will filter the list result
     * @param from   Parameter from the position
     * @param limit  Parameter that limits the number of results from the position
     * @return returns a T list or inherited from it correctly paginated ant filtered
     */
    <S> List<? extends DTO> get(S filter, int from, int limit);

    /**
     * Method update from the basic CRUD
     *
     * @param o  Object of type T that will be updated
     * @param pk The primary key of the object. First of all, the method must search the object
     *           to can update it
     * @return returns the object correctly updated if don't have any problem.
     */
    ResponseEntity<DTO> update(@Valid DTO o, PK pk) throws InstantiationException, IllegalAccessException;

    /**
     * Method delete from the basic CRUD
     *
     * @param pk The primary key of the object that will deleted.
     */
    void delete(PK pk) throws Exception;

    /**
     * Method count, it's a basic method added to the basic CRUD to facilitate the basic operations.
     *
     * @return returns the number of this object instances on the database
     */
    ResponseEntity<Long> count();

    /**
     * Method count that return the number of this object instances on the database, but applying a filter
     *
     * @param filter Filter with which the search will be done
     * @return returns the number of this object instances on the database applying the filter
     */
    <S> ResponseEntity<Long> countFilter(S filter);
}
