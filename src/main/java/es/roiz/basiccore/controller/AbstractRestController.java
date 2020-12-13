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

package es.roiz.basiccore.controller;

import es.roiz.basiccore.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractRestController<T, PK extends Serializable> implements Controller<T, PK> {

    protected final CrudService<T, PK> crudService;
    protected final RestTemplate restTemplate;

    @Autowired
    protected MessageSource messageSource;

    protected Class classType;

    public AbstractRestController(Class classType, CrudService<T, PK> crudService, RestTemplate restTemplate) {
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
    public ResponseEntity create(@Valid @RequestBody T o) {
        return ResponseEntity.status(HttpStatus.CREATED).body(crudService.create(o));
    }

    @Override
    @GetMapping(path = "{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'READ')")
    public ResponseEntity get(@PathVariable PK pk) {
        Optional<T> res = crudService.read(pk);
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
    public Iterable<T> get() {
        return crudService.list();
    }

    @Override
    @GetMapping(path = "{from}/{limit}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public List get(@PathVariable int from, @PathVariable int limit) {
        return crudService.list(from, limit).getContent();
    }

    @Override
    @GetMapping(path = "{filter}/{from}/{limit}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'LIST')")
    public List get(@PathVariable @NotNull String filter, @PathVariable @NotNull int from, @PathVariable @NotNull int limit) {
        return crudService.list(filter, from, limit).getContent();
    }

    @Override
    @PutMapping(path = "/{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'UPDATE')")
    public ResponseEntity update(@Valid @RequestBody T o, @PathVariable(name = "pk") PK pk) {
        return ResponseEntity.ok(crudService.update(o));
    }

    @Override
    @DeleteMapping(path = "/{pk}")
    @PreAuthorize("hasPermission(#this.this.getClassType().getSimpleName(),'DELETE')")
    public void delete(@PathVariable PK pk) throws Exception {
        if(!crudService.exists(pk)){
            throw new Exception("Not found");
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
    public ResponseEntity<Long> countFilter(@PathVariable String filter) {
        return ResponseEntity.ok(crudService.countFilter(filter));
    }
}
