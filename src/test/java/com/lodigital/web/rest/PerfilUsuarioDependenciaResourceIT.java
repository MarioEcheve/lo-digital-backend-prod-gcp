package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.PerfilUsuarioDependencia;
import com.lodigital.repository.PerfilUsuarioDependenciaRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PerfilUsuarioDependenciaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PerfilUsuarioDependenciaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private PerfilUsuarioDependenciaRepository perfilUsuarioDependenciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilUsuarioDependenciaMockMvc;

    private PerfilUsuarioDependencia perfilUsuarioDependencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuarioDependencia createEntity(EntityManager em) {
        PerfilUsuarioDependencia perfilUsuarioDependencia = new PerfilUsuarioDependencia()
            .nombre(DEFAULT_NOMBRE);
        return perfilUsuarioDependencia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuarioDependencia createUpdatedEntity(EntityManager em) {
        PerfilUsuarioDependencia perfilUsuarioDependencia = new PerfilUsuarioDependencia()
            .nombre(UPDATED_NOMBRE);
        return perfilUsuarioDependencia;
    }

    @BeforeEach
    public void initTest() {
        perfilUsuarioDependencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerfilUsuarioDependencia() throws Exception {
        int databaseSizeBeforeCreate = perfilUsuarioDependenciaRepository.findAll().size();
        // Create the PerfilUsuarioDependencia
        restPerfilUsuarioDependenciaMockMvc.perform(post("/api/perfil-usuario-dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(perfilUsuarioDependencia)))
            .andExpect(status().isCreated());

        // Validate the PerfilUsuarioDependencia in the database
        List<PerfilUsuarioDependencia> perfilUsuarioDependenciaList = perfilUsuarioDependenciaRepository.findAll();
        assertThat(perfilUsuarioDependenciaList).hasSize(databaseSizeBeforeCreate + 1);
        PerfilUsuarioDependencia testPerfilUsuarioDependencia = perfilUsuarioDependenciaList.get(perfilUsuarioDependenciaList.size() - 1);
        assertThat(testPerfilUsuarioDependencia.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createPerfilUsuarioDependenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = perfilUsuarioDependenciaRepository.findAll().size();

        // Create the PerfilUsuarioDependencia with an existing ID
        perfilUsuarioDependencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilUsuarioDependenciaMockMvc.perform(post("/api/perfil-usuario-dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(perfilUsuarioDependencia)))
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuarioDependencia in the database
        List<PerfilUsuarioDependencia> perfilUsuarioDependenciaList = perfilUsuarioDependenciaRepository.findAll();
        assertThat(perfilUsuarioDependenciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPerfilUsuarioDependencias() throws Exception {
        // Initialize the database
        perfilUsuarioDependenciaRepository.saveAndFlush(perfilUsuarioDependencia);

        // Get all the perfilUsuarioDependenciaList
        restPerfilUsuarioDependenciaMockMvc.perform(get("/api/perfil-usuario-dependencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilUsuarioDependencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getPerfilUsuarioDependencia() throws Exception {
        // Initialize the database
        perfilUsuarioDependenciaRepository.saveAndFlush(perfilUsuarioDependencia);

        // Get the perfilUsuarioDependencia
        restPerfilUsuarioDependenciaMockMvc.perform(get("/api/perfil-usuario-dependencias/{id}", perfilUsuarioDependencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfilUsuarioDependencia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingPerfilUsuarioDependencia() throws Exception {
        // Get the perfilUsuarioDependencia
        restPerfilUsuarioDependenciaMockMvc.perform(get("/api/perfil-usuario-dependencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerfilUsuarioDependencia() throws Exception {
        // Initialize the database
        perfilUsuarioDependenciaRepository.saveAndFlush(perfilUsuarioDependencia);

        int databaseSizeBeforeUpdate = perfilUsuarioDependenciaRepository.findAll().size();

        // Update the perfilUsuarioDependencia
        PerfilUsuarioDependencia updatedPerfilUsuarioDependencia = perfilUsuarioDependenciaRepository.findById(perfilUsuarioDependencia.getId()).get();
        // Disconnect from session so that the updates on updatedPerfilUsuarioDependencia are not directly saved in db
        em.detach(updatedPerfilUsuarioDependencia);
        updatedPerfilUsuarioDependencia
            .nombre(UPDATED_NOMBRE);

        restPerfilUsuarioDependenciaMockMvc.perform(put("/api/perfil-usuario-dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerfilUsuarioDependencia)))
            .andExpect(status().isOk());

        // Validate the PerfilUsuarioDependencia in the database
        List<PerfilUsuarioDependencia> perfilUsuarioDependenciaList = perfilUsuarioDependenciaRepository.findAll();
        assertThat(perfilUsuarioDependenciaList).hasSize(databaseSizeBeforeUpdate);
        PerfilUsuarioDependencia testPerfilUsuarioDependencia = perfilUsuarioDependenciaList.get(perfilUsuarioDependenciaList.size() - 1);
        assertThat(testPerfilUsuarioDependencia.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPerfilUsuarioDependencia() throws Exception {
        int databaseSizeBeforeUpdate = perfilUsuarioDependenciaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilUsuarioDependenciaMockMvc.perform(put("/api/perfil-usuario-dependencias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(perfilUsuarioDependencia)))
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuarioDependencia in the database
        List<PerfilUsuarioDependencia> perfilUsuarioDependenciaList = perfilUsuarioDependenciaRepository.findAll();
        assertThat(perfilUsuarioDependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerfilUsuarioDependencia() throws Exception {
        // Initialize the database
        perfilUsuarioDependenciaRepository.saveAndFlush(perfilUsuarioDependencia);

        int databaseSizeBeforeDelete = perfilUsuarioDependenciaRepository.findAll().size();

        // Delete the perfilUsuarioDependencia
        restPerfilUsuarioDependenciaMockMvc.perform(delete("/api/perfil-usuario-dependencias/{id}", perfilUsuarioDependencia.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PerfilUsuarioDependencia> perfilUsuarioDependenciaList = perfilUsuarioDependenciaRepository.findAll();
        assertThat(perfilUsuarioDependenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
