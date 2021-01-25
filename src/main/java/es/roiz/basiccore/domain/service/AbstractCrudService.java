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
import es.roiz.basiccore.infrastructure.DBEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer that connects the controller and the persistence.
 * This is the implementation of the CrudService Interface.
 *
 * @param <T>  The Class with which we will work from persistence
 * @param <PK> The id Type of the Class
 */
@Service("crudService")
public abstract class AbstractCrudService<DTO extends Dto, R extends PagingAndSortingRepository<T, PK>, T extends DBEntity, PK extends Serializable> implements CrudService<DTO, PK> {

    protected final R repository;
    protected final Class<T> entityType;
    protected final Class<DTO> dtoType;

    public AbstractCrudService(R repository, Class<T> entityType, Class<DTO> dtoType) {
        this.repository = repository;
        this.entityType = entityType;
        this.dtoType = dtoType;
    }

    @Override
    public DTO create(DTO o) throws IllegalAccessException, InstantiationException {
        T entity = (T) getClass().newInstance();
        BeanUtils.copyProperties(o, entity);
        entity = repository.save(entity);
        BeanUtils.copyProperties(entity, o);
        return o;
    }

    @Override
    public Iterable<DTO> create(Iterable<DTO> collection) throws IllegalAccessException, InstantiationException {

        Iterable<T> entities = new LinkedList<>();
        T entity;
        for (DTO dto : collection) {
            entity = entityType.newInstance();
            BeanUtils.copyProperties(dto, entity);
            ((List<T>) entities).add(entity);
        }

        entities = repository.saveAll(entities);


        ((List<DTO>) collection).clear();

        for (T e : entities) {
            DTO dto = dtoType.newInstance();
            BeanUtils.copyProperties(e, dto);
            ((List<DTO>) collection).add(dto);
        }

        return collection;
    }

    @Override
    public Optional<DTO> read(PK o) throws IllegalAccessException, InstantiationException {
        Optional<T> entity = repository.findById(o);
        if (entity.isPresent()) {
            DTO dto = dtoType.newInstance();
            BeanUtils.copyProperties(entity.get(), dto);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public DTO update(DTO o) throws IllegalAccessException, InstantiationException {
        T entity = entityType.newInstance();
        BeanUtils.copyProperties(o, entity);
        entity = repository.save(entity);
        BeanUtils.copyProperties(entity, o);
        return o;
    }

    @Override
    public void delete(DTO o) throws IllegalAccessException, InstantiationException {
        T entity = entityType.newInstance();
        BeanUtils.copyProperties(o, entity);
        repository.delete(entity);
    }

    @Override
    public void delete(PK o) throws SQLException {
        repository.deleteById(o);
    }

    @Override
    public Iterable<DTO> list() throws IllegalAccessException, InstantiationException {
        Iterable<T> returned = repository.findAll();
        List<DTO> res = new LinkedList<>();
        DTO dto;
        for (T entity : returned) {
            dto = dtoType.newInstance();
            BeanUtils.copyProperties(entity, dto);
            res.add(dto);
        }
        return res;
    }

    @Override
    public Page<DTO> list(int from, int limit) throws IllegalAccessException, InstantiationException {
        if (limit <= 0) {
            return new PageImpl<>(new LinkedList<>());
        }

        Page<T> returned = repository.findAll(PageRequest.of(from, limit));
        List<DTO> res = new LinkedList<>();
        DTO dto;
        for (T entity : returned) {
            dto = dtoType.newInstance();
            BeanUtils.copyProperties(entity, dto);
            res.add(dto);
        }
        return new PageImpl<>(res);
    }

    @Override
    public abstract <S> Page<? extends DTO> list(int from, int limit, S filter);

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public boolean exists(PK pk) {
        return repository.findById(pk).isPresent();
    }

    @Override
    public void updateAll(Collection<DTO> c) throws InstantiationException, IllegalAccessException {
        this.create(c);
    }

    @Override
    public abstract <S> Long countFilter(S filter);

}
