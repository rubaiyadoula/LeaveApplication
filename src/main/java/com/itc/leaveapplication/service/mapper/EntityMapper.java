package com.itc.leaveapplication.service.mapper;

import java.util.List;
import java.util.Optional;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper <D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List <E> toEntity(List<D> dtoList);

    List <D> toDto(List<E> entityList);
}
