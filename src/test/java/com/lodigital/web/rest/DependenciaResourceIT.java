package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.Dependencia;
import com.lodigital.repository.DependenciaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DependenciaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DependenciaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOMBRE_CONTACTO_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CONTACTO_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO_CONTACTO_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_CARGO_CONTACTO_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CONTACTO_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CONTACTO_COMERCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CONTACTO_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CONTACTO_TECNICO = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO_CONTACTO_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO_CONTACTO_TECNICO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_PRINCIPAL_CONTACTO_TECNICO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_SECUNDARIO_CONTACTO_TECNICO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CONTACTO_TECNICO = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CONTACTO_TECNICO = "BBBBBBBBBB";

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependenciaMockMvc;

    private Dependencia dependencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createEntity(EntityManager em) {
        Dependencia dependencia = new Dependencia()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .nombreContactoComercial(DEFAULT_NOMBRE_CONTACTO_COMERCIAL)
            .cargoContactoComercial(DEFAULT_CARGO_CONTACTO_COMERCIAL)
            .telefonoPrincipalContactoComercial(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL)
            .telefonoSecundarioContactoComercial(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL)
            .emailContactoComercial(DEFAULT_EMAIL_CONTACTO_COMERCIAL)
            .nombreContactoTecnico(DEFAULT_NOMBRE_CONTACTO_TECNICO)
            .cargoContactoTecnico(DEFAULT_CARGO_CONTACTO_TECNICO)
            .telefonoPrincipalContactoTecnico(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_TECNICO)
            .telefonoSecundarioContactoTecnico(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_TECNICO)
            .emailContactoTecnico(DEFAULT_EMAIL_CONTACTO_TECNICO);
        return dependencia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createUpdatedEntity(EntityManager em) {
        Dependencia dependencia = new Dependencia()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .nombreContactoComercial(UPDATED_NOMBRE_CONTACTO_COMERCIAL)
            .cargoContactoComercial(UPDATED_CARGO_CONTACTO_COMERCIAL)
            .telefonoPrincipalContactoComercial(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL)
            .telefonoSecundarioContactoComercial(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL)
            .emailContactoComercial(UPDATED_EMAIL_CONTACTO_COMERCIAL)
            .nombreContactoTecnico(UPDATED_NOMBRE_CONTACTO_TECNICO)
            .cargoContactoTecnico(UPDATED_CARGO_CONTACTO_TECNICO)
            .telefonoPrincipalContactoTecnico(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_TECNICO)
            .telefonoSecundarioContactoTecnico(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_TECNICO)
            .emailContactoTecnico(UPDATED_EMAIL_CONTACTO_TECNICO);
        return dependencia;
    }

    @BeforeEach
    public void initTest() {
        dependencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createDependencia() throws Exception {
        int databaseSizeBeforeCreate = dependenciaRepository.findAll().size();
        // Create the Dependencia
        restDependenciaMockMvc.perform(post("/api/dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isCreated());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDependencia.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDependencia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDependencia.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testDependencia.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);
        assertThat(testDependencia.getNombreContactoComercial()).isEqualTo(DEFAULT_NOMBRE_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getCargoContactoComercial()).isEqualTo(DEFAULT_CARGO_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getTelefonoPrincipalContactoComercial()).isEqualTo(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getTelefonoSecundarioContactoComercial()).isEqualTo(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getEmailContactoComercial()).isEqualTo(DEFAULT_EMAIL_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getNombreContactoTecnico()).isEqualTo(DEFAULT_NOMBRE_CONTACTO_TECNICO);
        assertThat(testDependencia.getCargoContactoTecnico()).isEqualTo(DEFAULT_CARGO_CONTACTO_TECNICO);
        assertThat(testDependencia.getTelefonoPrincipalContactoTecnico()).isEqualTo(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_TECNICO);
        assertThat(testDependencia.getTelefonoSecundarioContactoTecnico()).isEqualTo(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_TECNICO);
        assertThat(testDependencia.getEmailContactoTecnico()).isEqualTo(DEFAULT_EMAIL_CONTACTO_TECNICO);
    }

    @Test
    @Transactional
    public void createDependenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dependenciaRepository.findAll().size();

        // Create the Dependencia with an existing ID
        dependencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependenciaMockMvc.perform(post("/api/dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDependencias() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get all the dependenciaList
        restDependenciaMockMvc.perform(get("/api/dependencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].nombreContactoComercial").value(hasItem(DEFAULT_NOMBRE_CONTACTO_COMERCIAL)))
            .andExpect(jsonPath("$.[*].cargoContactoComercial").value(hasItem(DEFAULT_CARGO_CONTACTO_COMERCIAL)))
            .andExpect(jsonPath("$.[*].telefonoPrincipalContactoComercial").value(hasItem(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL)))
            .andExpect(jsonPath("$.[*].telefonoSecundarioContactoComercial").value(hasItem(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL)))
            .andExpect(jsonPath("$.[*].emailContactoComercial").value(hasItem(DEFAULT_EMAIL_CONTACTO_COMERCIAL)))
            .andExpect(jsonPath("$.[*].nombreContactoTecnico").value(hasItem(DEFAULT_NOMBRE_CONTACTO_TECNICO)))
            .andExpect(jsonPath("$.[*].cargoContactoTecnico").value(hasItem(DEFAULT_CARGO_CONTACTO_TECNICO)))
            .andExpect(jsonPath("$.[*].telefonoPrincipalContactoTecnico").value(hasItem(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_TECNICO)))
            .andExpect(jsonPath("$.[*].telefonoSecundarioContactoTecnico").value(hasItem(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_TECNICO)))
            .andExpect(jsonPath("$.[*].emailContactoTecnico").value(hasItem(DEFAULT_EMAIL_CONTACTO_TECNICO)));
    }
    
    @Test
    @Transactional
    public void getDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get the dependencia
        restDependenciaMockMvc.perform(get("/api/dependencias/{id}", dependencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependencia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.nombreContactoComercial").value(DEFAULT_NOMBRE_CONTACTO_COMERCIAL))
            .andExpect(jsonPath("$.cargoContactoComercial").value(DEFAULT_CARGO_CONTACTO_COMERCIAL))
            .andExpect(jsonPath("$.telefonoPrincipalContactoComercial").value(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL))
            .andExpect(jsonPath("$.telefonoSecundarioContactoComercial").value(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL))
            .andExpect(jsonPath("$.emailContactoComercial").value(DEFAULT_EMAIL_CONTACTO_COMERCIAL))
            .andExpect(jsonPath("$.nombreContactoTecnico").value(DEFAULT_NOMBRE_CONTACTO_TECNICO))
            .andExpect(jsonPath("$.cargoContactoTecnico").value(DEFAULT_CARGO_CONTACTO_TECNICO))
            .andExpect(jsonPath("$.telefonoPrincipalContactoTecnico").value(DEFAULT_TELEFONO_PRINCIPAL_CONTACTO_TECNICO))
            .andExpect(jsonPath("$.telefonoSecundarioContactoTecnico").value(DEFAULT_TELEFONO_SECUNDARIO_CONTACTO_TECNICO))
            .andExpect(jsonPath("$.emailContactoTecnico").value(DEFAULT_EMAIL_CONTACTO_TECNICO));
    }
    @Test
    @Transactional
    public void getNonExistingDependencia() throws Exception {
        // Get the dependencia
        restDependenciaMockMvc.perform(get("/api/dependencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // Update the dependencia
        Dependencia updatedDependencia = dependenciaRepository.findById(dependencia.getId()).get();
        // Disconnect from session so that the updates on updatedDependencia are not directly saved in db
        em.detach(updatedDependencia);
        updatedDependencia
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .nombreContactoComercial(UPDATED_NOMBRE_CONTACTO_COMERCIAL)
            .cargoContactoComercial(UPDATED_CARGO_CONTACTO_COMERCIAL)
            .telefonoPrincipalContactoComercial(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL)
            .telefonoSecundarioContactoComercial(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL)
            .emailContactoComercial(UPDATED_EMAIL_CONTACTO_COMERCIAL)
            .nombreContactoTecnico(UPDATED_NOMBRE_CONTACTO_TECNICO)
            .cargoContactoTecnico(UPDATED_CARGO_CONTACTO_TECNICO)
            .telefonoPrincipalContactoTecnico(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_TECNICO)
            .telefonoSecundarioContactoTecnico(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_TECNICO)
            .emailContactoTecnico(UPDATED_EMAIL_CONTACTO_TECNICO);

        restDependenciaMockMvc.perform(put("/api/dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDependencia)))
            .andExpect(status().isOk());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDependencia.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDependencia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDependencia.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testDependencia.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
        assertThat(testDependencia.getNombreContactoComercial()).isEqualTo(UPDATED_NOMBRE_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getCargoContactoComercial()).isEqualTo(UPDATED_CARGO_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getTelefonoPrincipalContactoComercial()).isEqualTo(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getTelefonoSecundarioContactoComercial()).isEqualTo(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getEmailContactoComercial()).isEqualTo(UPDATED_EMAIL_CONTACTO_COMERCIAL);
        assertThat(testDependencia.getNombreContactoTecnico()).isEqualTo(UPDATED_NOMBRE_CONTACTO_TECNICO);
        assertThat(testDependencia.getCargoContactoTecnico()).isEqualTo(UPDATED_CARGO_CONTACTO_TECNICO);
        assertThat(testDependencia.getTelefonoPrincipalContactoTecnico()).isEqualTo(UPDATED_TELEFONO_PRINCIPAL_CONTACTO_TECNICO);
        assertThat(testDependencia.getTelefonoSecundarioContactoTecnico()).isEqualTo(UPDATED_TELEFONO_SECUNDARIO_CONTACTO_TECNICO);
        assertThat(testDependencia.getEmailContactoTecnico()).isEqualTo(UPDATED_EMAIL_CONTACTO_TECNICO);
    }

    @Test
    @Transactional
    public void updateNonExistingDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenciaMockMvc.perform(put("/api/dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeDelete = dependenciaRepository.findAll().size();

        // Delete the dependencia
        restDependenciaMockMvc.perform(delete("/api/dependencias/{id}", dependencia.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
