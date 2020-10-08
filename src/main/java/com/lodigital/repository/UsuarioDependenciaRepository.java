package com.lodigital.repository;

import com.lodigital.domain.UsuarioDependencia;
import com.lodigital.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.Map;

/**
 * Spring Data  repository for the UsuarioDependencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioDependenciaRepository extends JpaRepository<UsuarioDependencia, Long> {

    @Query("select usuarioDependencia from UsuarioDependencia usuarioDependencia where usuarioDependencia.usuario.login = ?#{principal.username}")
    List<UsuarioDependencia> findByUsuarioIsCurrentUser();

    @Query("select a from UsuarioDependencia a "+
            "inner join a.usuario b "+
            "where b.id = :idUsuario")
    List<UsuarioDependencia> findUserByUsuarioDependencia(@Param("idUsuario") Long idUsuario);

    @Query("select a from UsuarioDependencia a "+
            "inner join a.dependencia b "+
            "where b.id = :idDependencia")
    List<UsuarioDependencia> findUserByUsuarioDependencia2(@Param("idDependencia") Long idDependencia);

    @Query(value="select c2.id , c2.fecha_inicio_servicio , c2.fecha_termino_servicio, " +
                    "c2.fecha_termino_acceso, c2.observaciones_servicio, c2.codigo, "+
                    "c2.nombre, c2.descripcion, c2.direccion, m2.id as id_modalidad, " +
                    "m2.nombre as modalidad, tc.id as id_tipo_contrato, tc.nombre as tipo_contrato, " +
                    " es.id as id_estado_servicio, es.nombre as estado_servicio " +
                        " from usuario_libro ul " +
                        "inner join usuario_dependencia ud ON ud.id = ul.usuario_dependencia_id "+
                        "inner join jhi_user ju on ju.id = ud.usuario_id "+
                        "inner join libro l2 on l2.id = ul.libro_id "+
                        "inner join contrato c2 on c2.id = l2.contrato_id "+ 
                        "inner join modalidad m2 on m2.id = c2.modalidad_id "+
                        "inner join tipo_contrato tc on tc.id = c2.tipo_contrato_id "+ 
                        "left join estado_servicio es on es.id = c2.estado_servicio_id " +
                        "where ju.id = :idUsuario "+
                        "group by c2.id , " +
                            "c2.fecha_inicio_servicio , "+
                            "c2.fecha_termino_servicio, "+
                            "c2.fecha_termino_acceso, "+
                            "c2.observaciones_servicio, "+
                            "c2.codigo, "+
                            "c2.nombre, " +
                            "c2.descripcion, " +
                            "c2.direccion, " +
                            "m2.id, " +
                            "m2.nombre, " +
                            "tc.id, " +  
                            "tc.nombre, " +
                            "es.id, " +
                            "es.nombre ", nativeQuery = true)
    List<Map<String, String>> findUserByUsuarioDependenciaRolUser(@Param("idUsuario") Long idUsuario);

    @Query(value="select " +
                    "c.id , " +
                    "c.fecha_inicio_servicio , " +
                    "c.fecha_termino_servicio, " +
                    "c.fecha_termino_acceso, " +
                    "c.observaciones_servicio, " +
                    "c.codigo, " +
                    "c.nombre, " +
                    "c.descripcion, " +
                    "c.direccion, " +
                    "m2.id as id_modalidad, " +
                    "m2.nombre as modalidad, " +
                    "tc.id as id_tipo_contrato, " +
                    "tc.nombre as tipo_contrato, " +
                    "es.id as id_estado_servicio, " +
                    "es.nombre as estado_servicio  " +
                "from contrato c  " +
                "inner join dependencia dm on dm.id = c.dependencia_mandante_id " +
                "inner join dependencia dc on dc.id = c.id_dependencia_contratista " +
                "inner join modalidad m2 on m2.id = c.modalidad_id " +
                "inner join tipo_contrato tc on tc.id = c.tipo_contrato_id " +
                "left join estado_servicio es on es.id = c.estado_servicio_id " +
                "where dm.id = :idDependencia or dc.id = :idDependencia ", nativeQuery = true)
    List<Map<String, String>> findContratosByDependencia(@Param("idDependencia") Long idDependencia);

    @Query(value="SELECT "+
                        "c.id , "+
                        "c.fecha_inicio_servicio ,"+
                        "c.fecha_termino_servicio, "+
                        "c.fecha_termino_acceso, "+
                        "c.observaciones_servicio, "+
                        "c.codigo, "+
                        "c.nombre, "+
                        "c.descripcion, "+
                        "c.direccion, "+
                        "m2.id as id_modalidad, "+
                        "m2.nombre as modalidad, "+
                        "tc.id as id_tipo_contrato, "+
                        "tc.nombre as tipo_contrato, "+
                        "es.id as id_estado_servicio, "+
                        "es.nombre as estado_servicio  "+
                        "FROM contrato c "+
                    "inner join libro l on l.contrato_id  = c.id "+
                    "inner join usuario_libro ul on ul.libro_id  = l.id "+
                    "inner join usuario_dependencia ud on ud.id = ul.usuario_dependencia_id "+
                    "inner join jhi_user ju on ju.id = ud.usuario_id   "+
                    "inner join modalidad m2 on m2.id = c.modalidad_id "+
                    "inner join tipo_contrato tc on tc.id = c.tipo_contrato_id "+
                    "left join estado_servicio es on es.id = c.estado_servicio_id "+
                    "where ju.id = :idUsuario and ul.estado = true "+
                    "group by c.id , "+
                        "c.fecha_inicio_servicio ,"+
                        "c.fecha_termino_servicio, "+
                        "c.fecha_termino_acceso, "+
                        "c.observaciones_servicio, "+
                        "c.codigo, "+
                        "c.nombre, "+
                        "c.descripcion, "+
                        "c.direccion, "+
                        "m2.id , "+
                        "m2.nombre , "+
                        "tc.id , "+
                        "tc.nombre , "+
                        "es.id , "+
                        "es.nombre", nativeQuery = true)
    List<Map<String, String>> findContratosByUsuarioNormal(@Param("idUsuario") Long idUsuario);


    @Query("select a.password from User a where a.id = :idUsuario")
    String buscarClavePorUsuario(@Param("idUsuario") Long idUsuario);
}
