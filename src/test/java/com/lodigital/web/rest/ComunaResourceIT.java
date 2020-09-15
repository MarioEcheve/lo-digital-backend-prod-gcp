package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.Comuna;
import com.lodigital.repository.ComunaRepository;

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
 * Integration tests for the {@link ComunaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ComunaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComunaMockMvc;

    private Comuna comuna;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comuna createEntity(EntityManager em) {
        Comuna comuna = new Comuna()
            .nombre(DEFAULT_NOMBRE);
        return comuna;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comuna createUpdatedEntity(EntityManager em) {
        Comuna comuna = new Comuna()
            .nombre(UPDATED_NOMBRE);
        return comuna;
    }

    @BeforeEach
    public void initTest() {
        comuna = createEntity(em);
    }

    @Test
    @Transactional
    public void createComuna() throws Exception {
        int databaseSizeBeforeCreate = comunaRepository.findAll().size();
        // Create the Comuna
        restComunaMockMvc.perform(post("/api/comunas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comuna)))
            .andExpect(status().isCreated());

        // Validate the Comuna in the database
        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeCreate + 1);
        Comuna testComuna = comunaList.get(comunaList.size() - 1);
        assertThat(testComuna.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createComunaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comunaRepository.findAll().size();

        // Create the Comuna with an existing ID
        comuna.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComunaMockMvc.perform(post("/api/comunas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comuna)))
            .andExpect(status().isBadRequest());

        // Validate the Comuna in the database
        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = comunaRepository.findAll().size();
        // set the field null
        comuna.setNombre(null);

        // Create the Comuna, which fails.


        restComunaMockMvc.perform(post("/api/comunas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comuna)))
            .andExpect(status().isBadRequest());

        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComunas() throws Exception {
        // Initialize the database
        comunaRepository.saveAndFlush(comuna);

        // Get all the comunaList
        restComunaMockMvc.perform(get("/api/comunas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comuna.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getComuna() throws Exception {
        // Initialize the database
        comunaRepository.saveAndFlush(comuna);

        // Get the comuna
        restComunaMockMvc.perform(get("/api/comunas/{id}", comuna.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comuna.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingComuna() throws Exception {
        // Get the comuna
        restComunaMockMvc.perform(get("/api/comunas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComuna() throws Exception {
        // Initialize the database
        comunaRepository.saveAndFlush(comuna);

        int databaseSizeBeforeUpdate = comunaRepository.findAll().size();

        // Update the comuna
        Comuna updatedComuna = comunaRepository.findById(comuna.getId()).get();
        // Disconnect from session so that the updates on updatedComuna are not directly saved in db
        em.detach(updatedComuna);
        updatedComuna
            .nombre(UPDATED_NOMBRE);

        restComunaMockMvc.perform(put("/api/comunas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedComuna)))
            .andExpect(status().isOk());

        // Validate the Comuna in the database
        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeUpdate);
        Comuna testComuna = comunaList.get(comunaList.size() - 1);
        assertThat(testComuna.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingComuna() throws Exception {
        int databaseSizeBeforeUpdate = comunaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComunaMockMvc.perform(put("/api/comunas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comuna)))
            .andExpect(status().isBadRequest());

        // Validate the Comuna in the database
        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComuna() throws Exception {
        // Initialize the database
        comunaRepository.saveAndFlush(comuna);

        int databaseSizeBeforeDelete = comunaRepository.findAll().size();

        // Delete the comuna
        restComunaMockMvc.perform(delete("/api/comunas/{id}", comuna.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comuna> comunaList = comunaRepository.findAll();
        assertThat(comunaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
