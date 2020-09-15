package com.lodigital.repository;

import com.lodigital.domain.TipoMonto;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TipoMonto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoMontoRepository extends JpaRepository<TipoMonto, Long> {
}
