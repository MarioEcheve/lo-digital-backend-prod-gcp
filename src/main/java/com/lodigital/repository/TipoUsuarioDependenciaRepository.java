package com.lodigital.repository;

import com.lodigital.domain.TipoUsuarioDependencia;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TipoUsuarioDependencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoUsuarioDependenciaRepository extends JpaRepository<TipoUsuarioDependencia, Long> {
}
