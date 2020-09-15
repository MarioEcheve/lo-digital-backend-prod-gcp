package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.EstadoLibro;
import com.lodigital.repository.EstadoLibroRepository;

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
 * Integration tests for the {@link EstadoLibroResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EstadoLibroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private EstadoLibroRepository estadoLibroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoLibroMockMvc;

    private EstadoLibro estadoLibro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoLibro createEntity(EntityManager em) {
        EstadoLibro estadoLibro = new EstadoLibro()
            .nombre(DEFAULT_NOMBRE);
        return estadoLibro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoLibro createUpdatedEntity(EntityManager em) {
        EstadoLibro estadoLibro = new EstadoLibro()
            .nombre(UPDATED_NOMBRE);
        return estadoLibro;
    }

    @BeforeEach
    public void initTest() {
        estadoLibro = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstadoLibro() throws Exception {
        int databaseSizeBeforeCreate = estadoLibroRepository.findAll().size();
        // Create the EstadoLibro
        restEstadoLibroMockMvc.perform(post("/api/estado-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoLibro)))
            .andExpect(status().isCreated());

        // Validate the EstadoLibro in the database
        List<EstadoLibro> estadoLibroList = estadoLibroRepository.findAll();
        assertThat(estadoLibroList).hasSize(databaseSizeBeforeCreate + 1);
        EstadoLibro testEstadoLibro = estadoLibroList.get(estadoLibroList.size() - 1);
        assertThat(testEstadoLibro.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createEstadoLibroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estadoLibroRepository.findAll().size();

        // Create the EstadoLibro with an existing ID
        estadoLibro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoLibroMockMvc.perform(post("/api/estado-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoLibro)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoLibro in the database
        List<EstadoLibro> estadoLibroList = estadoLibroRepository.findAll();
        assertThat(estadoLibroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEstadoLibros() throws Exception {
        // Initialize the database
        estadoLibroRepository.saveAndFlush(estadoLibro);

        // Get all the estadoLibroList
        restEstadoLibroMockMvc.perform(get("/api/estado-libros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoLibro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getEstadoLibro() throws Exception {
        // Initialize the database
        estadoLibroRepository.saveAndFlush(estadoLibro);

        // Get the estadoLibro
        restEstadoLibroMockMvc.perform(get("/api/estado-libros/{id}", estadoLibro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoLibro.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingEstadoLibro() throws Exception {
        // Get the estadoLibro
        restEstadoLibroMockMvc.perform(get("/api/estado-libros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstadoLibro() throws Exception {
        // Initialize the database
        estadoLibroRepository.saveAndFlush(estadoLibro);

        int databaseSizeBeforeUpdate = estadoLibroRepository.findAll().size();

        // Update the estadoLibro
        EstadoLibro updatedEstadoLibro = estadoLibroRepository.findById(estadoLibro.getId()).get();
        // Disconnect from session so that the updates on updatedEstadoLibro are not directly saved in db
        em.detach(updatedEstadoLibro);
        updatedEstadoLibro
            .nombre(UPDATED_NOMBRE);

        restEstadoLibroMockMvc.perform(put("/api/estado-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEstadoLibro)))
            .andExpect(status().isOk());

        // Validate the EstadoLibro in the database
        List<EstadoLibro> estadoLibroList = estadoLibroRepository.findAll();
        assertThat(estadoLibroList).hasSize(databaseSizeBeforeUpdate);
        EstadoLibro testEstadoLibro = estadoLibroList.get(estadoLibroList.size() - 1);
        assertThat(testEstadoLibro.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingEstadoLibro() throws Exception {
        int databaseSizeBeforeUpdate = estadoLibroRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoLibroMockMvc.perform(put("/api/estado-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoLibro)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoLibro in the database
        List<EstadoLibro> estadoLibroList = estadoLibroRepository.findAll();
        assertThat(estadoLibroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEstadoLibro() throws Exception {
        // Initialize the database
        estadoLibroRepository.saveAndFlush(estadoLibro);

        int databaseSizeBeforeDelete = estadoLibroRepository.findAll().size();

        // Delete the estadoLibro
        restEstadoLibroMockMvc.perform(delete("/api/estado-libros/{id}", estadoLibro.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EstadoLibro> estadoLibroList = estadoLibroRepository.findAll();
        assertThat(estadoLibroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
