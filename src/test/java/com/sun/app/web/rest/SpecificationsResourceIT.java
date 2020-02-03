package com.sun.app.web.rest;

import com.sun.app.SunApp;
import com.sun.app.domain.Specifications;
import com.sun.app.domain.Product;
import com.sun.app.repository.SpecificationsRepository;
import com.sun.app.service.SpecificationsService;
import com.sun.app.service.dto.SpecificationsDTO;
import com.sun.app.service.mapper.SpecificationsMapper;
import com.sun.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.sun.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SpecificationsResource} REST controller.
 */
@SpringBootTest(classes = SunApp.class)
public class SpecificationsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SpecificationsRepository specificationsRepository;

    @Autowired
    private SpecificationsMapper specificationsMapper;

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSpecificationsMockMvc;

    private Specifications specifications;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpecificationsResource specificationsResource = new SpecificationsResource(specificationsService);
        this.restSpecificationsMockMvc = MockMvcBuilders.standaloneSetup(specificationsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specifications createEntity(EntityManager em) {
        Specifications specifications = new Specifications()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        specifications.setProduct(product);
        return specifications;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specifications createUpdatedEntity(EntityManager em) {
        Specifications specifications = new Specifications()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        specifications.setProduct(product);
        return specifications;
    }

    @BeforeEach
    public void initTest() {
        specifications = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecifications() throws Exception {
        int databaseSizeBeforeCreate = specificationsRepository.findAll().size();

        // Create the Specifications
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);
        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isCreated());

        // Validate the Specifications in the database
        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeCreate + 1);
        Specifications testSpecifications = specificationsList.get(specificationsList.size() - 1);
        assertThat(testSpecifications.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpecifications.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSpecifications.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSpecifications.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createSpecificationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specificationsRepository.findAll().size();

        // Create the Specifications with an existing ID
        specifications.setId(1L);
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Specifications in the database
        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificationsRepository.findAll().size();
        // set the field null
        specifications.setTitle(null);

        // Create the Specifications, which fails.
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificationsRepository.findAll().size();
        // set the field null
        specifications.setContent(null);

        // Create the Specifications, which fails.
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificationsRepository.findAll().size();
        // set the field null
        specifications.setCreatedAt(null);

        // Create the Specifications, which fails.
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificationsRepository.findAll().size();
        // set the field null
        specifications.setUpdatedAt(null);

        // Create the Specifications, which fails.
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        restSpecificationsMockMvc.perform(post("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecifications() throws Exception {
        // Initialize the database
        specificationsRepository.saveAndFlush(specifications);

        // Get all the specificationsList
        restSpecificationsMockMvc.perform(get("/api/specifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getSpecifications() throws Exception {
        // Initialize the database
        specificationsRepository.saveAndFlush(specifications);

        // Get the specifications
        restSpecificationsMockMvc.perform(get("/api/specifications/{id}", specifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(specifications.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecifications() throws Exception {
        // Get the specifications
        restSpecificationsMockMvc.perform(get("/api/specifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecifications() throws Exception {
        // Initialize the database
        specificationsRepository.saveAndFlush(specifications);

        int databaseSizeBeforeUpdate = specificationsRepository.findAll().size();

        // Update the specifications
        Specifications updatedSpecifications = specificationsRepository.findById(specifications.getId()).get();
        // Disconnect from session so that the updates on updatedSpecifications are not directly saved in db
        em.detach(updatedSpecifications);
        updatedSpecifications
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(updatedSpecifications);

        restSpecificationsMockMvc.perform(put("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isOk());

        // Validate the Specifications in the database
        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeUpdate);
        Specifications testSpecifications = specificationsList.get(specificationsList.size() - 1);
        assertThat(testSpecifications.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpecifications.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSpecifications.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecifications.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecifications() throws Exception {
        int databaseSizeBeforeUpdate = specificationsRepository.findAll().size();

        // Create the Specifications
        SpecificationsDTO specificationsDTO = specificationsMapper.toDto(specifications);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecificationsMockMvc.perform(put("/api/specifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specificationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Specifications in the database
        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecifications() throws Exception {
        // Initialize the database
        specificationsRepository.saveAndFlush(specifications);

        int databaseSizeBeforeDelete = specificationsRepository.findAll().size();

        // Delete the specifications
        restSpecificationsMockMvc.perform(delete("/api/specifications/{id}", specifications.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specifications> specificationsList = specificationsRepository.findAll();
        assertThat(specificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
