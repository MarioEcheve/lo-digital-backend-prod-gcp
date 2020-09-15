package com.lodigital.repository;

import com.lodigital.domain.Modalidad;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Modalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModalidadRepository extends JpaRepository<Modalidad, Long> {
}
