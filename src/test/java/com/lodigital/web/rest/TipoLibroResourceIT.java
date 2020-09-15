package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.TipoLibro;
import com.lodigital.repository.TipoLibroRepository;

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
 * Integration tests for the {@link TipoLibroResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TipoLibroResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoLibroRepository tipoLibroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoLibroMockMvc;

    private TipoLibro tipoLibro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoLibro createEntity(EntityManager em) {
        TipoLibro tipoLibro = new TipoLibro()
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoLibro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoLibro createUpdatedEntity(EntityManager em) {
        TipoLibro tipoLibro = new TipoLibro()
            .descripcion(UPDATED_DESCRIPCION);
        return tipoLibro;
    }

    @BeforeEach
    public void initTest() {
        tipoLibro = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoLibro() throws Exception {
        int databaseSizeBeforeCreate = tipoLibroRepository.findAll().size();
        // Create the TipoLibro
        restTipoLibroMockMvc.perform(post("/api/tipo-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoLibro)))
            .andExpect(status().isCreated());

        // Validate the TipoLibro in the database
        List<TipoLibro> tipoLibroList = tipoLibroRepository.findAll();
        assertThat(tipoLibroList).hasSize(databaseSizeBeforeCreate + 1);
        TipoLibro testTipoLibro = tipoLibroList.get(tipoLibroList.size() - 1);
        assertThat(testTipoLibro.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createTipoLibroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoLibroRepository.findAll().size();

        // Create the TipoLibro with an existing ID
        tipoLibro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoLibroMockMvc.perform(post("/api/tipo-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoLibro)))
            .andExpect(status().isBadRequest());

        // Validate the TipoLibro in the database
        List<TipoLibro> tipoLibroList = tipoLibroRepository.findAll();
        assertThat(tipoLibroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTipoLibros() throws Exception {
        // Initialize the database
        tipoLibroRepository.saveAndFlush(tipoLibro);

        // Get all the tipoLibroList
        restTipoLibroMockMvc.perform(get("/api/tipo-libros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoLibro.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getTipoLibro() throws Exception {
        // Initialize the database
        tipoLibroRepository.saveAndFlush(tipoLibro);

        // Get the tipoLibro
        restTipoLibroMockMvc.perform(get("/api/tipo-libros/{id}", tipoLibro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoLibro.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }
    @Test
    @Transactional
    public void getNonExistingTipoLibro() throws Exception {
        // Get the tipoLibro
        restTipoLibroMockMvc.perform(get("/api/tipo-libros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoLibro() throws Exception {
        // Initialize the database
        tipoLibroRepository.saveAndFlush(tipoLibro);

        int databaseSizeBeforeUpdate = tipoLibroRepository.findAll().size();

        // Update the tipoLibro
        TipoLibro updatedTipoLibro = tipoLibroRepository.findById(tipoLibro.getId()).get();
        // Disconnect from session so that the updates on updatedTipoLibro are not directly saved in db
        em.detach(updatedTipoLibro);
        updatedTipoLibro
            .descripcion(UPDATED_DESCRIPCION);

        restTipoLibroMockMvc.perform(put("/api/tipo-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoLibro)))
            .andExpect(status().isOk());

        // Validate the TipoLibro in the database
        List<TipoLibro> tipoLibroList = tipoLibroRepository.findAll();
        assertThat(tipoLibroList).hasSize(databaseSizeBeforeUpdate);
        TipoLibro testTipoLibro = tipoLibroList.get(tipoLibroList.size() - 1);
        assertThat(testTipoLibro.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoLibro() throws Exception {
        int databaseSizeBeforeUpdate = tipoLibroRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoLibroMockMvc.perform(put("/api/tipo-libros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoLibro)))
            .andExpect(status().isBadRequest());

        // Validate the TipoLibro in the database
        List<TipoLibro> tipoLibroList = tipoLibroRepository.findAll();
        assertThat(tipoLibroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoLibro() throws Exception {
        // Initialize the database
        tipoLibroRepository.saveAndFlush(tipoLibro);

        int databaseSizeBeforeDelete = tipoLibroRepository.findAll().size();

        // Delete the tipoLibro
        restTipoLibroMockMvc.perform(delete("/api/tipo-libros/{id}", tipoLibro.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoLibro> tipoLibroList = tipoLibroRepository.findAll();
        assertThat(tipoLibroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
