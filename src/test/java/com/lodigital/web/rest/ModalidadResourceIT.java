package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.Modalidad;
import com.lodigital.repository.ModalidadRepository;

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
 * Integration tests for the {@link ModalidadResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ModalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private ModalidadRepository modalidadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModalidadMockMvc;

    private Modalidad modalidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modalidad createEntity(EntityManager em) {
        Modalidad modalidad = new Modalidad()
            .nombre(DEFAULT_NOMBRE);
        return modalidad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modalidad createUpdatedEntity(EntityManager em) {
        Modalidad modalidad = new Modalidad()
            .nombre(UPDATED_NOMBRE);
        return modalidad;
    }

    @BeforeEach
    public void initTest() {
        modalidad = createEntity(em);
    }

    @Test
    @Transactional
    public void createModalidad() throws Exception {
        int databaseSizeBeforeCreate = modalidadRepository.findAll().size();
        // Create the Modalidad
        restModalidadMockMvc.perform(post("/api/modalidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modalidad)))
            .andExpect(status().isCreated());

        // Validate the Modalidad in the database
        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeCreate + 1);
        Modalidad testModalidad = modalidadList.get(modalidadList.size() - 1);
        assertThat(testModalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createModalidadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modalidadRepository.findAll().size();

        // Create the Modalidad with an existing ID
        modalidad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModalidadMockMvc.perform(post("/api/modalidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modalidad)))
            .andExpect(status().isBadRequest());

        // Validate the Modalidad in the database
        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = modalidadRepository.findAll().size();
        // set the field null
        modalidad.setNombre(null);

        // Create the Modalidad, which fails.


        restModalidadMockMvc.perform(post("/api/modalidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modalidad)))
            .andExpect(status().isBadRequest());

        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModalidads() throws Exception {
        // Initialize the database
        modalidadRepository.saveAndFlush(modalidad);

        // Get all the modalidadList
        restModalidadMockMvc.perform(get("/api/modalidads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getModalidad() throws Exception {
        // Initialize the database
        modalidadRepository.saveAndFlush(modalidad);

        // Get the modalidad
        restModalidadMockMvc.perform(get("/api/modalidads/{id}", modalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modalidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingModalidad() throws Exception {
        // Get the modalidad
        restModalidadMockMvc.perform(get("/api/modalidads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModalidad() throws Exception {
        // Initialize the database
        modalidadRepository.saveAndFlush(modalidad);

        int databaseSizeBeforeUpdate = modalidadRepository.findAll().size();

        // Update the modalidad
        Modalidad updatedModalidad = modalidadRepository.findById(modalidad.getId()).get();
        // Disconnect from session so that the updates on updatedModalidad are not directly saved in db
        em.detach(updatedModalidad);
        updatedModalidad
            .nombre(UPDATED_NOMBRE);

        restModalidadMockMvc.perform(put("/api/modalidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedModalidad)))
            .andExpect(status().isOk());

        // Validate the Modalidad in the database
        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeUpdate);
        Modalidad testModalidad = modalidadList.get(modalidadList.size() - 1);
        assertThat(testModalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingModalidad() throws Exception {
        int databaseSizeBeforeUpdate = modalidadRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModalidadMockMvc.perform(put("/api/modalidads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modalidad)))
            .andExpect(status().isBadRequest());

        // Validate the Modalidad in the database
        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModalidad() throws Exception {
        // Initialize the database
        modalidadRepository.saveAndFlush(modalidad);

        int databaseSizeBeforeDelete = modalidadRepository.findAll().size();

        // Delete the modalidad
        restModalidadMockMvc.perform(delete("/api/modalidads/{id}", modalidad.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Modalidad> modalidadList = modalidadRepository.findAll();
        assertThat(modalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
