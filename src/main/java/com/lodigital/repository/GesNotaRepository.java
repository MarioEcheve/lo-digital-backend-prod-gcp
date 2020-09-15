package com.lodigital.repository;

import com.lodigital.domain.GesNota;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GesNota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GesNotaRepository extends JpaRepository<GesNota, Long> {
}
