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

package es.roiz.basiccore.application.rest;

import es.roiz.basiccore.application.Controller;
import es.roiz.basiccore.domain.dto.Dto;
import es.roiz.basiccore.domain.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRestController<DTO extends Dto, PK extends Serializable> implements Controller<DTO, PK> {

    protected final CrudService<DTO, PK> crudService;
    protected final RestTemplate restTemplate;

    @Autowired
    protected MessageSource messageSource;

    protected Class classType;

    public AbstractRestController(Class classType, CrudService<DTO, PK> crudService, RestTemplate restTemplate) {
        this.classType = classType;
        this.crudService = crudService;
        this.restTemplate = restTemplate;
    }

    public Class getClassType() {
        return classType;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'CREATE')")
    public ResponseEntity create(@Valid @RequestBody DTO o) throws InstantiationException, IllegalAccessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(crudService.create(o));
    }

    @Override
    @GetMapping(path = "{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'READ')")
    public ResponseEntity get(@PathVariable PK pk) throws InstantiationException, IllegalAccessException {
        Optional<DTO> res = crudService.read(pk);
        try {
            return ResponseEntity.ok(res.get());
        } catch (Exception e) {
            try {
                return ResponseEntity.ok(getClassType().newInstance());
            } catch (InstantiationException | IllegalAccessException er) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

    @Override
    @GetMapping
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public Iterable<DTO> get() throws InstantiationException, IllegalAccessException {
        return crudService.list();
    }

    @Override
    @GetMapping(path = "{from}/{limit}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public List get(@PathVariable int from, @PathVariable int limit) throws InstantiationException, IllegalAccessException {
        return crudService.list(from, limit).getContent();
    }

    @Override
    @GetMapping(path = "{filter}/{from}/{limit}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public <S> List get(@PathVariable @NotNull S filter, @PathVariable @NotNull int from, @PathVariable @NotNull int limit) {
        Page<? extends DTO> page = crudService.list(from, limit, filter);
        if (page != null) {
            return page.getContent();
        }
        return new LinkedList<>();
    }

    @Override
    @PutMapping(path = "/{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'UPDATE')")
    public ResponseEntity update(@Valid @RequestBody DTO o, @PathVariable(name = "pk") PK pk) throws InstantiationException, IllegalAccessException {
        return ResponseEntity.ok(crudService.update(o));
    }

    @Override
    @DeleteMapping(path = "/{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'DELETE')")
    public void delete(@PathVariable PK pk) throws Exception {
        if (!crudService.exists(pk)) {
            throw new EntityNotFoundException();
        }
        crudService.delete(pk);
    }

    @Override
    @GetMapping(path = "/count")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(crudService.count());
    }

    @Override
    @GetMapping(path = "/count/{filter}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public <S> ResponseEntity<Long> countFilter(@PathVariable S filter) {
        return ResponseEntity.ok(crudService.countFilter(filter));
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            InstantiationException.class,
            IllegalAccessException.class
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleEntityExistsException(EntityNotFoundException e) {
        return e.getMessage();
    }
}
