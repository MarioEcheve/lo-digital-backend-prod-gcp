package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.GesNota;
import com.lodigital.repository.GesNotaRepository;

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
 * Integration tests for the {@link GesNotaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GesNotaResourceIT {

    private static final String DEFAULT_NOTA = "AAAAAAAAAA";
    private static final String UPDATED_NOTA = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private GesNotaRepository gesNotaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGesNotaMockMvc;

    private GesNota gesNota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GesNota createEntity(EntityManager em) {
        GesNota gesNota = new GesNota()
            .nota(DEFAULT_NOTA)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
        return gesNota;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GesNota createUpdatedEntity(EntityManager em) {
        GesNota gesNota = new GesNota()
            .nota(UPDATED_NOTA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        return gesNota;
    }

    @BeforeEach
    public void initTest() {
        gesNota = createEntity(em);
    }

    @Test
    @Transactional
    public void createGesNota() throws Exception {
        int databaseSizeBeforeCreate = gesNotaRepository.findAll().size();
        // Create the GesNota
        restGesNotaMockMvc.perform(post("/api/ges-notas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gesNota)))
            .andExpect(status().isCreated());

        // Validate the GesNota in the database
        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeCreate + 1);
        GesNota testGesNota = gesNotaList.get(gesNotaList.size() - 1);
        assertThat(testGesNota.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testGesNota.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testGesNota.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void createGesNotaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gesNotaRepository.findAll().size();

        // Create the GesNota with an existing ID
        gesNota.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGesNotaMockMvc.perform(post("/api/ges-notas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gesNota)))
            .andExpect(status().isBadRequest());

        // Validate the GesNota in the database
        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNotaIsRequired() throws Exception {
        int databaseSizeBeforeTest = gesNotaRepository.findAll().size();
        // set the field null
        gesNota.setNota(null);

        // Create the GesNota, which fails.


        restGesNotaMockMvc.perform(post("/api/ges-notas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gesNota)))
            .andExpect(status().isBadRequest());

        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGesNotas() throws Exception {
        // Initialize the database
        gesNotaRepository.saveAndFlush(gesNota);

        // Get all the gesNotaList
        restGesNotaMockMvc.perform(get("/api/ges-notas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gesNota.getId().intValue())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }
    
    @Test
    @Transactional
    public void getGesNota() throws Exception {
        // Initialize the database
        gesNotaRepository.saveAndFlush(gesNota);

        // Get the gesNota
        restGesNotaMockMvc.perform(get("/api/ges-notas/{id}", gesNota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gesNota.getId().intValue()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingGesNota() throws Exception {
        // Get the gesNota
        restGesNotaMockMvc.perform(get("/api/ges-notas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGesNota() throws Exception {
        // Initialize the database
        gesNotaRepository.saveAndFlush(gesNota);

        int databaseSizeBeforeUpdate = gesNotaRepository.findAll().size();

        // Update the gesNota
        GesNota updatedGesNota = gesNotaRepository.findById(gesNota.getId()).get();
        // Disconnect from session so that the updates on updatedGesNota are not directly saved in db
        em.detach(updatedGesNota);
        updatedGesNota
            .nota(UPDATED_NOTA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restGesNotaMockMvc.perform(put("/api/ges-notas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGesNota)))
            .andExpect(status().isOk());

        // Validate the GesNota in the database
        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeUpdate);
        GesNota testGesNota = gesNotaList.get(gesNotaList.size() - 1);
        assertThat(testGesNota.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testGesNota.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testGesNota.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void updateNonExistingGesNota() throws Exception {
        int databaseSizeBeforeUpdate = gesNotaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGesNotaMockMvc.perform(put("/api/ges-notas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gesNota)))
            .andExpect(status().isBadRequest());

        // Validate the GesNota in the database
        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGesNota() throws Exception {
        // Initialize the database
        gesNotaRepository.saveAndFlush(gesNota);

        int databaseSizeBeforeDelete = gesNotaRepository.findAll().size();

        // Delete the gesNota
        restGesNotaMockMvc.perform(delete("/api/ges-notas/{id}", gesNota.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GesNota> gesNotaList = gesNotaRepository.findAll();
        assertThat(gesNotaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
