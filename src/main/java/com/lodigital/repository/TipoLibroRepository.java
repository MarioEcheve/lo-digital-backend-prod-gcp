package com.lodigital.repository;

import com.lodigital.domain.TipoLibro;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TipoLibro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoLibroRepository extends JpaRepository<TipoLibro, Long> {
}
