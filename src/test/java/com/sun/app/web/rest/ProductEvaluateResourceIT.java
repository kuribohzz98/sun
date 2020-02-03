package com.sun.app.web.rest;

import com.sun.app.SunApp;
import com.sun.app.domain.ProductEvaluate;
import com.sun.app.domain.User;
import com.sun.app.domain.Product;
import com.sun.app.repository.ProductEvaluateRepository;
import com.sun.app.service.ProductEvaluateService;
import com.sun.app.service.dto.ProductEvaluateDTO;
import com.sun.app.service.mapper.ProductEvaluateMapper;
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
 * Integration tests for the {@link ProductEvaluateResource} REST controller.
 */
@SpringBootTest(classes = SunApp.class)
public class ProductEvaluateResourceIT {

    private static final Integer DEFAULT_POINT = 0;
    private static final Integer UPDATED_POINT = 1;

    private static final String DEFAULT_EVALUATE = "AAAAAAAAAA";
    private static final String UPDATED_EVALUATE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductEvaluateRepository productEvaluateRepository;

    @Autowired
    private ProductEvaluateMapper productEvaluateMapper;

    @Autowired
    private ProductEvaluateService productEvaluateService;

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

    private MockMvc restProductEvaluateMockMvc;

    private ProductEvaluate productEvaluate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductEvaluateResource productEvaluateResource = new ProductEvaluateResource(productEvaluateService);
        this.restProductEvaluateMockMvc = MockMvcBuilders.standaloneSetup(productEvaluateResource)
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
    public static ProductEvaluate createEntity(EntityManager em) {
        ProductEvaluate productEvaluate = new ProductEvaluate()
            .point(DEFAULT_POINT)
            .evaluate(DEFAULT_EVALUATE)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        productEvaluate.setUser(user);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productEvaluate.setProduct(product);
        return productEvaluate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductEvaluate createUpdatedEntity(EntityManager em) {
        ProductEvaluate productEvaluate = new ProductEvaluate()
            .point(UPDATED_POINT)
            .evaluate(UPDATED_EVALUATE)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        productEvaluate.setUser(user);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productEvaluate.setProduct(product);
        return productEvaluate;
    }

    @BeforeEach
    public void initTest() {
        productEvaluate = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductEvaluate() throws Exception {
        int databaseSizeBeforeCreate = productEvaluateRepository.findAll().size();

        // Create the ProductEvaluate
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(productEvaluate);
        restProductEvaluateMockMvc.perform(post("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductEvaluate in the database
        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeCreate + 1);
        ProductEvaluate testProductEvaluate = productEvaluateList.get(productEvaluateList.size() - 1);
        assertThat(testProductEvaluate.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testProductEvaluate.getEvaluate()).isEqualTo(DEFAULT_EVALUATE);
        assertThat(testProductEvaluate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createProductEvaluateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productEvaluateRepository.findAll().size();

        // Create the ProductEvaluate with an existing ID
        productEvaluate.setId(1L);
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(productEvaluate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductEvaluateMockMvc.perform(post("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductEvaluate in the database
        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointIsRequired() throws Exception {
        int databaseSizeBeforeTest = productEvaluateRepository.findAll().size();
        // set the field null
        productEvaluate.setPoint(null);

        // Create the ProductEvaluate, which fails.
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(productEvaluate);

        restProductEvaluateMockMvc.perform(post("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isBadRequest());

        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productEvaluateRepository.findAll().size();
        // set the field null
        productEvaluate.setCreatedAt(null);

        // Create the ProductEvaluate, which fails.
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(productEvaluate);

        restProductEvaluateMockMvc.perform(post("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isBadRequest());

        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductEvaluates() throws Exception {
        // Initialize the database
        productEvaluateRepository.saveAndFlush(productEvaluate);

        // Get all the productEvaluateList
        restProductEvaluateMockMvc.perform(get("/api/product-evaluates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productEvaluate.getId().intValue())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].evaluate").value(hasItem(DEFAULT_EVALUATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getProductEvaluate() throws Exception {
        // Initialize the database
        productEvaluateRepository.saveAndFlush(productEvaluate);

        // Get the productEvaluate
        restProductEvaluateMockMvc.perform(get("/api/product-evaluates/{id}", productEvaluate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productEvaluate.getId().intValue()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT))
            .andExpect(jsonPath("$.evaluate").value(DEFAULT_EVALUATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductEvaluate() throws Exception {
        // Get the productEvaluate
        restProductEvaluateMockMvc.perform(get("/api/product-evaluates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductEvaluate() throws Exception {
        // Initialize the database
        productEvaluateRepository.saveAndFlush(productEvaluate);

        int databaseSizeBeforeUpdate = productEvaluateRepository.findAll().size();

        // Update the productEvaluate
        ProductEvaluate updatedProductEvaluate = productEvaluateRepository.findById(productEvaluate.getId()).get();
        // Disconnect from session so that the updates on updatedProductEvaluate are not directly saved in db
        em.detach(updatedProductEvaluate);
        updatedProductEvaluate
            .point(UPDATED_POINT)
            .evaluate(UPDATED_EVALUATE)
            .createdAt(UPDATED_CREATED_AT);
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(updatedProductEvaluate);

        restProductEvaluateMockMvc.perform(put("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isOk());

        // Validate the ProductEvaluate in the database
        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeUpdate);
        ProductEvaluate testProductEvaluate = productEvaluateList.get(productEvaluateList.size() - 1);
        assertThat(testProductEvaluate.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testProductEvaluate.getEvaluate()).isEqualTo(UPDATED_EVALUATE);
        assertThat(testProductEvaluate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingProductEvaluate() throws Exception {
        int databaseSizeBeforeUpdate = productEvaluateRepository.findAll().size();

        // Create the ProductEvaluate
        ProductEvaluateDTO productEvaluateDTO = productEvaluateMapper.toDto(productEvaluate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductEvaluateMockMvc.perform(put("/api/product-evaluates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productEvaluateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductEvaluate in the database
        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductEvaluate() throws Exception {
        // Initialize the database
        productEvaluateRepository.saveAndFlush(productEvaluate);

        int databaseSizeBeforeDelete = productEvaluateRepository.findAll().size();

        // Delete the productEvaluate
        restProductEvaluateMockMvc.perform(delete("/api/product-evaluates/{id}", productEvaluate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductEvaluate> productEvaluateList = productEvaluateRepository.findAll();
        assertThat(productEvaluateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
