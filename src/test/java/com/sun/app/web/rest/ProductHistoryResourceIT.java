package com.sun.app.web.rest;

import com.sun.app.SunApp;
import com.sun.app.domain.ProductHistory;
import com.sun.app.domain.Product;
import com.sun.app.repository.ProductHistoryRepository;
import com.sun.app.service.ProductHistoryService;
import com.sun.app.service.dto.ProductHistoryDTO;
import com.sun.app.service.mapper.ProductHistoryMapper;
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
 * Integration tests for the {@link ProductHistoryResource} REST controller.
 */
@SpringBootTest(classes = SunApp.class)
public class ProductHistoryResourceIT {

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private ProductHistoryMapper productHistoryMapper;

    @Autowired
    private ProductHistoryService productHistoryService;

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

    private MockMvc restProductHistoryMockMvc;

    private ProductHistory productHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductHistoryResource productHistoryResource = new ProductHistoryResource(productHistoryService);
        this.restProductHistoryMockMvc = MockMvcBuilders.standaloneSetup(productHistoryResource)
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
    public static ProductHistory createEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .data(DEFAULT_DATA)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .description(DEFAULT_DESCRIPTION)
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
        productHistory.setProduct(product);
        return productHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductHistory createUpdatedEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .data(UPDATED_DATA)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .description(UPDATED_DESCRIPTION)
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
        productHistory.setProduct(product);
        return productHistory;
    }

    @BeforeEach
    public void initTest() {
        productHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductHistory() throws Exception {
        int databaseSizeBeforeCreate = productHistoryRepository.findAll().size();

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);
        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testProductHistory.getOldValue()).isEqualTo(DEFAULT_OLD_VALUE);
        assertThat(testProductHistory.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testProductHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductHistory.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProductHistory.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createProductHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productHistoryRepository.findAll().size();

        // Create the ProductHistory with an existing ID
        productHistory.setId(1L);
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().size();
        // set the field null
        productHistory.setData(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNewValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().size();
        // set the field null
        productHistory.setNewValue(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().size();
        // set the field null
        productHistory.setCreatedAt(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().size();
        // set the field null
        productHistory.setUpdatedAt(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        restProductHistoryMockMvc.perform(post("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductHistories() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        // Get all the productHistoryList
        restProductHistoryMockMvc.perform(get("/api/product-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        // Get the productHistory
        restProductHistoryMockMvc.perform(get("/api/product-histories/{id}", productHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productHistory.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductHistory() throws Exception {
        // Get the productHistory
        restProductHistoryMockMvc.perform(get("/api/product-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().size();

        // Update the productHistory
        ProductHistory updatedProductHistory = productHistoryRepository.findById(productHistory.getId()).get();
        // Disconnect from session so that the updates on updatedProductHistory are not directly saved in db
        em.detach(updatedProductHistory);
        updatedProductHistory
            .data(UPDATED_DATA)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(updatedProductHistory);

        restProductHistoryMockMvc.perform(put("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testProductHistory.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testProductHistory.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testProductHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductHistory.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProductHistory.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().size();

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc.perform(put("/api/product-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        int databaseSizeBeforeDelete = productHistoryRepository.findAll().size();

        // Delete the productHistory
        restProductHistoryMockMvc.perform(delete("/api/product-histories/{id}", productHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
