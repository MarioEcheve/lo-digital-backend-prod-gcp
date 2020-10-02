package com.lodigital.repository;

import com.lodigital.domain.Comuna;
import com.lodigital.domain.Contrato;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
/**
 * Spring Data  repository for the Contrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("select a from Comuna a "
        +"inner join a.region b "
        +"where b.id = :id")
    List<Comuna> buscaComunaPorRegion(@Param("id") Long id);  

    @Query("select a from Contrato a "
        +"left join a.dependenciaMandante b "
        +"where b.id = :idDependencia or a.idDependenciaContratista = :idDependencia")
    List<Contrato> buscaContratoPorDependencia(@Param("idDependencia") Long idDependencia);  
    

}
