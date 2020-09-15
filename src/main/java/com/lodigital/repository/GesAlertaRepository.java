package com.lodigital.repository;

import com.lodigital.domain.GesAlerta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GesAlerta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GesAlertaRepository extends JpaRepository<GesAlerta, Long> {
}
