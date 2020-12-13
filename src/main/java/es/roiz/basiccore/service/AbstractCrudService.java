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

package es.roiz.basiccore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Service layer that connects the controller and the persistence.
 * This is the implementation of the CrudService Interface.
 *
 * @param <T>  The Class with which we will work from persistence
 * @param <PK> The id Type of the Class
 */
@Service("crudService")
public abstract class AbstractCrudService<R extends PagingAndSortingRepository<T, PK>, T, PK extends Serializable> implements CrudService<T, PK> {

    protected final R repository;

    public AbstractCrudService(R repository) {
        this.repository = repository;
    }

    @Override
    public T create(T o) {
        return repository.save(o);
    }

    @Override
    public Iterable<T> create(Iterable<T> collection) {
        return repository.saveAll(collection);
    }

    @Override
    public Optional<T> read(PK o) {
        return repository.findById(o);
    }

    @Override
    public T update(T o) {
        return repository.save(o);
    }

    @Override
    public void delete(T o) {
        repository.delete(o);
    }

    @Override
    public void delete(PK o) throws SQLException {
        repository.deleteById(o);
    }

    @Override
    public Iterable<T> list() {
        return repository.findAll();
    }

    @Override
    public Page<T> list(int from, int limit) {
        if (limit > 0) {
            return repository.findAll(PageRequest.of(from, limit));
        }
        return null;
    }

    @Override
    public abstract Page<? extends T> list(String filter, int from, int limit);

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public boolean exists(PK pk) {
        return repository.findById(pk).isPresent();
    }

    @Override
    public void updateAll(Collection<T> c) {
        repository.saveAll(c);
    }

    @Override
    public abstract Long countFilter(String filter);

}
