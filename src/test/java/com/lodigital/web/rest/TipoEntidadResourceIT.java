package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.TipoEntidad;
import com.lodigital.repository.TipoEntidadRepository;

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
 * Integration tests for the {@link TipoEntidadResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TipoEntidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private TipoEntidadRepository tipoEntidadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoEntidadMockMvc;

    private TipoEntidad tipoEntidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoEntidad createEntity(EntityManager em) {
        TipoEntidad tipoEntidad = new TipoEntidad()
            .nombre(DEFAULT_NOMBRE);
        return tipoEntidad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoEntidad createUpdatedEntity(EntityManager em) {
        TipoEntidad tipoEntidad = new TipoEntidad()
            .nombre(UPDATED_NOMBRE);
        return tipoEntidad;
    }

    @BeforeEach
    public void initTest() {
        tipoEntidad = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoEntidad() throws Exception {
        int databaseSizeBeforeCreate = tipoEntidadRepository.findAll().size();
        // Create the TipoEntidad
        restTipoEntidadMockMvc.perform(post("/api/tipo-entidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoEntidad)))
            .andExpect(status().isCreated());

        // Validate the TipoEntidad in the database
        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeCreate + 1);
        TipoEntidad testTipoEntidad = tipoEntidadList.get(tipoEntidadList.size() - 1);
        assertThat(testTipoEntidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createTipoEntidadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoEntidadRepository.findAll().size();

        // Create the TipoEntidad with an existing ID
        tipoEntidad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoEntidadMockMvc.perform(post("/api/tipo-entidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoEntidad)))
            .andExpect(status().isBadRequest());

        // Validate the TipoEntidad in the database
        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoEntidadRepository.findAll().size();
        // set the field null
        tipoEntidad.setNombre(null);

        // Create the TipoEntidad, which fails.


        restTipoEntidadMockMvc.perform(post("/api/tipo-entidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoEntidad)))
            .andExpect(status().isBadRequest());

        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoEntidads() throws Exception {
        // Initialize the database
        tipoEntidadRepository.saveAndFlush(tipoEntidad);

        // Get all the tipoEntidadList
        restTipoEntidadMockMvc.perform(get("/api/tipo-entidads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoEntidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getTipoEntidad() throws Exception {
        // Initialize the database
        tipoEntidadRepository.saveAndFlush(tipoEntidad);

        // Get the tipoEntidad
        restTipoEntidadMockMvc.perform(get("/api/tipo-entidads/{id}", tipoEntidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoEntidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingTipoEntidad() throws Exception {
        // Get the tipoEntidad
        restTipoEntidadMockMvc.perform(get("/api/tipo-entidads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoEntidad() throws Exception {
        // Initialize the database
        tipoEntidadRepository.saveAndFlush(tipoEntidad);

        int databaseSizeBeforeUpdate = tipoEntidadRepository.findAll().size();

        // Update the tipoEntidad
        TipoEntidad updatedTipoEntidad = tipoEntidadRepository.findById(tipoEntidad.getId()).get();
        // Disconnect from session so that the updates on updatedTipoEntidad are not directly saved in db
        em.detach(updatedTipoEntidad);
        updatedTipoEntidad
            .nombre(UPDATED_NOMBRE);

        restTipoEntidadMockMvc.perform(put("/api/tipo-entidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoEntidad)))
            .andExpect(status().isOk());

        // Validate the TipoEntidad in the database
        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeUpdate);
        TipoEntidad testTipoEntidad = tipoEntidadList.get(tipoEntidadList.size() - 1);
        assertThat(testTipoEntidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoEntidad() throws Exception {
        int databaseSizeBeforeUpdate = tipoEntidadRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoEntidadMockMvc.perform(put("/api/tipo-entidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoEntidad)))
            .andExpect(status().isBadRequest());

        // Validate the TipoEntidad in the database
        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoEntidad() throws Exception {
        // Initialize the database
        tipoEntidadRepository.saveAndFlush(tipoEntidad);

        int databaseSizeBeforeDelete = tipoEntidadRepository.findAll().size();

        // Delete the tipoEntidad
        restTipoEntidadMockMvc.perform(delete("/api/tipo-entidads/{id}", tipoEntidad.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoEntidad> tipoEntidadList = tipoEntidadRepository.findAll();
        assertThat(tipoEntidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
