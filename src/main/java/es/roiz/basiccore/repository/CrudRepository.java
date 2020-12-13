package es.roiz.basiccore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CrudRepository<T, PK extends Serializable> extends PagingAndSortingRepository<T, PK> {

    <S> List<S> findBy(Class<S> classType);

    <S> Page<S> findBy(Pageable pageable, Class<S> classType);
}
