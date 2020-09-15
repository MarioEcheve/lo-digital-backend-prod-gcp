package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.TipoFirma;
import com.lodigital.repository.TipoFirmaRepository;

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
 * Integration tests for the {@link TipoFirmaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TipoFirmaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private TipoFirmaRepository tipoFirmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoFirmaMockMvc;

    private TipoFirma tipoFirma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFirma createEntity(EntityManager em) {
        TipoFirma tipoFirma = new TipoFirma()
            .nombre(DEFAULT_NOMBRE);
        return tipoFirma;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoFirma createUpdatedEntity(EntityManager em) {
        TipoFirma tipoFirma = new TipoFirma()
            .nombre(UPDATED_NOMBRE);
        return tipoFirma;
    }

    @BeforeEach
    public void initTest() {
        tipoFirma = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoFirma() throws Exception {
        int databaseSizeBeforeCreate = tipoFirmaRepository.findAll().size();
        // Create the TipoFirma
        restTipoFirmaMockMvc.perform(post("/api/tipo-firmas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoFirma)))
            .andExpect(status().isCreated());

        // Validate the TipoFirma in the database
        List<TipoFirma> tipoFirmaList = tipoFirmaRepository.findAll();
        assertThat(tipoFirmaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoFirma testTipoFirma = tipoFirmaList.get(tipoFirmaList.size() - 1);
        assertThat(testTipoFirma.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createTipoFirmaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoFirmaRepository.findAll().size();

        // Create the TipoFirma with an existing ID
        tipoFirma.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoFirmaMockMvc.perform(post("/api/tipo-firmas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoFirma)))
            .andExpect(status().isBadRequest());

        // Validate the TipoFirma in the database
        List<TipoFirma> tipoFirmaList = tipoFirmaRepository.findAll();
        assertThat(tipoFirmaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTipoFirmas() throws Exception {
        // Initialize the database
        tipoFirmaRepository.saveAndFlush(tipoFirma);

        // Get all the tipoFirmaList
        restTipoFirmaMockMvc.perform(get("/api/tipo-firmas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoFirma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getTipoFirma() throws Exception {
        // Initialize the database
        tipoFirmaRepository.saveAndFlush(tipoFirma);

        // Get the tipoFirma
        restTipoFirmaMockMvc.perform(get("/api/tipo-firmas/{id}", tipoFirma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoFirma.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingTipoFirma() throws Exception {
        // Get the tipoFirma
        restTipoFirmaMockMvc.perform(get("/api/tipo-firmas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoFirma() throws Exception {
        // Initialize the database
        tipoFirmaRepository.saveAndFlush(tipoFirma);

        int databaseSizeBeforeUpdate = tipoFirmaRepository.findAll().size();

        // Update the tipoFirma
        TipoFirma updatedTipoFirma = tipoFirmaRepository.findById(tipoFirma.getId()).get();
        // Disconnect from session so that the updates on updatedTipoFirma are not directly saved in db
        em.detach(updatedTipoFirma);
        updatedTipoFirma
            .nombre(UPDATED_NOMBRE);

        restTipoFirmaMockMvc.perform(put("/api/tipo-firmas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoFirma)))
            .andExpect(status().isOk());

        // Validate the TipoFirma in the database
        List<TipoFirma> tipoFirmaList = tipoFirmaRepository.findAll();
        assertThat(tipoFirmaList).hasSize(databaseSizeBeforeUpdate);
        TipoFirma testTipoFirma = tipoFirmaList.get(tipoFirmaList.size() - 1);
        assertThat(testTipoFirma.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoFirma() throws Exception {
        int databaseSizeBeforeUpdate = tipoFirmaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoFirmaMockMvc.perform(put("/api/tipo-firmas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoFirma)))
            .andExpect(status().isBadRequest());

        // Validate the TipoFirma in the database
        List<TipoFirma> tipoFirmaList = tipoFirmaRepository.findAll();
        assertThat(tipoFirmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoFirma() throws Exception {
        // Initialize the database
        tipoFirmaRepository.saveAndFlush(tipoFirma);

        int databaseSizeBeforeDelete = tipoFirmaRepository.findAll().size();

        // Delete the tipoFirma
        restTipoFirmaMockMvc.perform(delete("/api/tipo-firmas/{id}", tipoFirma.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoFirma> tipoFirmaList = tipoFirmaRepository.findAll();
        assertThat(tipoFirmaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
