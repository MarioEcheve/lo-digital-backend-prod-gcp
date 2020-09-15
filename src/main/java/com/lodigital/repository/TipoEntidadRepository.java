package com.lodigital.repository;

import com.lodigital.domain.TipoEntidad;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TipoEntidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoEntidadRepository extends JpaRepository<TipoEntidad, Long> {
}
