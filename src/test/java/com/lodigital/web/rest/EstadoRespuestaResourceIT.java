package com.lodigital.web.rest;

import com.lodigital.BackendApp;
import com.lodigital.domain.EstadoRespuesta;
import com.lodigital.repository.EstadoRespuestaRepository;

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
 * Integration tests for the {@link EstadoRespuestaResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EstadoRespuestaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private EstadoRespuestaRepository estadoRespuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoRespuestaMockMvc;

    private EstadoRespuesta estadoRespuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoRespuesta createEntity(EntityManager em) {
        EstadoRespuesta estadoRespuesta = new EstadoRespuesta()
            .nombre(DEFAULT_NOMBRE);
        return estadoRespuesta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoRespuesta createUpdatedEntity(EntityManager em) {
        EstadoRespuesta estadoRespuesta = new EstadoRespuesta()
            .nombre(UPDATED_NOMBRE);
        return estadoRespuesta;
    }

    @BeforeEach
    public void initTest() {
        estadoRespuesta = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstadoRespuesta() throws Exception {
        int databaseSizeBeforeCreate = estadoRespuestaRepository.findAll().size();
        // Create the EstadoRespuesta
        restEstadoRespuestaMockMvc.perform(post("/api/estado-respuestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoRespuesta)))
            .andExpect(status().isCreated());

        // Validate the EstadoRespuesta in the database
        List<EstadoRespuesta> estadoRespuestaList = estadoRespuestaRepository.findAll();
        assertThat(estadoRespuestaList).hasSize(databaseSizeBeforeCreate + 1);
        EstadoRespuesta testEstadoRespuesta = estadoRespuestaList.get(estadoRespuestaList.size() - 1);
        assertThat(testEstadoRespuesta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createEstadoRespuestaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estadoRespuestaRepository.findAll().size();

        // Create the EstadoRespuesta with an existing ID
        estadoRespuesta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoRespuestaMockMvc.perform(post("/api/estado-respuestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoRespuesta)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoRespuesta in the database
        List<EstadoRespuesta> estadoRespuestaList = estadoRespuestaRepository.findAll();
        assertThat(estadoRespuestaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEstadoRespuestas() throws Exception {
        // Initialize the database
        estadoRespuestaRepository.saveAndFlush(estadoRespuesta);

        // Get all the estadoRespuestaList
        restEstadoRespuestaMockMvc.perform(get("/api/estado-respuestas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoRespuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getEstadoRespuesta() throws Exception {
        // Initialize the database
        estadoRespuestaRepository.saveAndFlush(estadoRespuesta);

        // Get the estadoRespuesta
        restEstadoRespuestaMockMvc.perform(get("/api/estado-respuestas/{id}", estadoRespuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoRespuesta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingEstadoRespuesta() throws Exception {
        // Get the estadoRespuesta
        restEstadoRespuestaMockMvc.perform(get("/api/estado-respuestas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstadoRespuesta() throws Exception {
        // Initialize the database
        estadoRespuestaRepository.saveAndFlush(estadoRespuesta);

        int databaseSizeBeforeUpdate = estadoRespuestaRepository.findAll().size();

        // Update the estadoRespuesta
        EstadoRespuesta updatedEstadoRespuesta = estadoRespuestaRepository.findById(estadoRespuesta.getId()).get();
        // Disconnect from session so that the updates on updatedEstadoRespuesta are not directly saved in db
        em.detach(updatedEstadoRespuesta);
        updatedEstadoRespuesta
            .nombre(UPDATED_NOMBRE);

        restEstadoRespuestaMockMvc.perform(put("/api/estado-respuestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEstadoRespuesta)))
            .andExpect(status().isOk());

        // Validate the EstadoRespuesta in the database
        List<EstadoRespuesta> estadoRespuestaList = estadoRespuestaRepository.findAll();
        assertThat(estadoRespuestaList).hasSize(databaseSizeBeforeUpdate);
        EstadoRespuesta testEstadoRespuesta = estadoRespuestaList.get(estadoRespuestaList.size() - 1);
        assertThat(testEstadoRespuesta.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingEstadoRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = estadoRespuestaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoRespuestaMockMvc.perform(put("/api/estado-respuestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(estadoRespuesta)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoRespuesta in the database
        List<EstadoRespuesta> estadoRespuestaList = estadoRespuestaRepository.findAll();
        assertThat(estadoRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEstadoRespuesta() throws Exception {
        // Initialize the database
        estadoRespuestaRepository.saveAndFlush(estadoRespuesta);

        int databaseSizeBeforeDelete = estadoRespuestaRepository.findAll().size();

        // Delete the estadoRespuesta
        restEstadoRespuestaMockMvc.perform(delete("/api/estado-respuestas/{id}", estadoRespuesta.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EstadoRespuesta> estadoRespuestaList = estadoRespuestaRepository.findAll();
        assertThat(estadoRespuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
