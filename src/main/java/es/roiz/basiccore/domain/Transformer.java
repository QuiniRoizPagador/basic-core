package es.roiz.basiccore.domain;

import es.roiz.basiccore.domain.dto.Dto;
import es.roiz.basiccore.infrastructure.DBEntity;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Transformer {
    public static <T extends DBEntity, DTO extends Dto> Iterable<DTO> transformToDTO(Iterable<T> iterable, Class<DTO> dtoType)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<DTO> res = new LinkedList<>();
        DTO dto;
        for (T entity : iterable) {
            dto = dtoType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            res.add(dto);
        }
        return res;
    }

    public static <T extends DBEntity, DTO extends Dto> Iterable<T> transformToEntity(Iterable<DTO> iterable, Class<T> entityType)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> res = new LinkedList<>();
        T entity;
        for (DTO dto : iterable) {
            entity = entityType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            res.add(entity);
        }
        return res;
    }

    public static <T extends DBEntity, DTO extends Dto> DTO transformToDTO(T entity, Class<DTO> dtoType)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        DTO dto = dtoType.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static <T extends DBEntity, DTO extends Dto> T transformToEntity(DTO dto, Class<T> entityType)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T entity = entityType.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
