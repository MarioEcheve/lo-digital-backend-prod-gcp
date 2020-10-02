package com.lodigital.repository;

import com.lodigital.domain.Libro;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data  repository for the Libro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("select a from Libro a "
        +"inner join a.contrato b "
        +"where b.id = :idContrato")
    List<Libro> buscarlibroPorContrato(@Param("idContrato") Long idContrato);

    @Query("select a from Libro a "
        +"inner join a.usuarioLibros b "
        +"inner join b.usuarioDependencia c "
        +"inner join c.usuario d "
        +"where d.id = :idUsuario and b.estado = true")
    List<Libro> getMisLibros(@Param("idUsuario") Long idUsuario);

    @Query("select a from Libro a "
        +"left join a.usuarioLibros b "
        +"inner join b.usuarioDependencia c "
        +"inner join c.usuario d "
        +"where d.id = :idUsuario and a.contrato.id = :idContrato")
    List<Libro> getMisLibrosContratoDetalle(@Param("idUsuario") Long idUsuario,@Param("idContrato") Long idContrato);
}
