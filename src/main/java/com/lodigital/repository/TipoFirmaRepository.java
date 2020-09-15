package com.lodigital.repository;

import com.lodigital.domain.TipoFirma;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TipoFirma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoFirmaRepository extends JpaRepository<TipoFirma, Long> {
}
