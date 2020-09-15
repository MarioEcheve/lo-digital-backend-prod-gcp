package com.lodigital.repository;

import com.lodigital.domain.FolioReferencia;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.Map;
/**
 * Spring Data  repository for the FolioReferencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FolioReferenciaRepository extends JpaRepository<FolioReferencia, Long> {
    @Query("select  a from FolioReferencia a where  a.idFolioOrigen =:id")
    List<FolioReferencia> buscaFoliosPorFolioOrigen(@Param("id") Long id);
}
