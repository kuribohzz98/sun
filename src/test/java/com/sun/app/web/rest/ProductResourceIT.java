package com.sun.app.web.rest;

import com.sun.app.SunApp;
import com.sun.app.domain.Product;
import com.sun.app.domain.ProductType;
import com.sun.app.domain.Provider;
import com.sun.app.repository.ProductRepository;
import com.sun.app.service.ProductService;
import com.sun.app.service.dto.ProductDTO;
import com.sun.app.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = SunApp.class)
public class ProductResourceIT {

    private static final Integer DEFAULT_PRODUCT_TYPE_ID = 1;
    private static final Integer UPDATED_PRODUCT_TYPE_ID = 2;

    private static final Integer DEFAULT_PROVIDER_ID = 1;
    private static final Integer UPDATED_PROVIDER_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_SELL_PRICE = 1L;
    private static final Long UPDATED_SELL_PRICE = 2L;

    private static final Long DEFAULT_IMPORT_PRICE = 1L;
    private static final Long UPDATED_IMPORT_PRICE = 2L;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_PRODUCT_LINE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_LINE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHOTO_ID = 1;
    private static final Integer UPDATED_PHOTO_ID = 2;

    private static final Integer DEFAULT_POINT = 1;
    private static final Integer UPDATED_POINT = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

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

    private MockMvc restProductMockMvc;

    private Product product;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productTypeId(DEFAULT_PRODUCT_TYPE_ID)
            .providerId(DEFAULT_PROVIDER_ID)
            .photoId(DEFAULT_PHOTO_ID)
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .sellPrice(DEFAULT_SELL_PRICE)
            .importPrice(DEFAULT_IMPORT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .productLine(DEFAULT_PRODUCT_LINE)
            .salePrice(DEFAULT_POINT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        ProductType productType;
        if (TestUtil.findAll(em, ProductType.class).isEmpty()) {
            productType = ProductTypeResourceIT.createEntity(em);
            em.persist(productType);
            em.flush();
        } else {
            productType = TestUtil.findAll(em, ProductType.class).get(0);
        }
        product.setProductType(productType);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        product.setProvider(provider);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .productTypeId(UPDATED_PRODUCT_TYPE_ID)
            .providerId(UPDATED_PROVIDER_ID)
            .photoId(UPDATED_PHOTO_ID)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .sellPrice(UPDATED_SELL_PRICE)
            .importPrice(UPDATED_IMPORT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .productLine(UPDATED_PRODUCT_LINE)
            .salePrice(UPDATED_POINT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        ProductType productType;
        if (TestUtil.findAll(em, ProductType.class).isEmpty()) {
            productType = ProductTypeResourceIT.createUpdatedEntity(em);
            em.persist(productType);
            em.flush();
        } else {
            productType = TestUtil.findAll(em, ProductType.class).get(0);
        }
        product.setProductType(productType);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        product.setProvider(provider);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductTypeId()).isEqualTo(DEFAULT_PRODUCT_TYPE_ID);
        assertThat(testProduct.getProviderId()).isEqualTo(DEFAULT_PROVIDER_ID);
        assertThat(testProduct.getPhotoId()).isEqualTo(DEFAULT_PHOTO_ID);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getSellPrice()).isEqualTo(DEFAULT_SELL_PRICE);
        assertThat(testProduct.getImportPrice()).isEqualTo(DEFAULT_IMPORT_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProduct.getProductLine()).isEqualTo(DEFAULT_PRODUCT_LINE);
        assertThat(testProduct.getSalePrice()).isEqualTo(DEFAULT_POINT);
        assertThat(testProduct.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProductTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductTypeId(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProviderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProviderId(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSellPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSellPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImportPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setImportPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setQuantity(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setCreatedAt(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setUpdatedAt(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productTypeId").value(hasItem(DEFAULT_PRODUCT_TYPE_ID)))
            .andExpect(jsonPath("$.[*].providerId").value(hasItem(DEFAULT_PROVIDER_ID)))
            .andExpect(jsonPath("$.[*].photoId").value(hasItem(DEFAULT_PHOTO_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(DEFAULT_SELL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].importPrice").value(hasItem(DEFAULT_IMPORT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productLine").value(hasItem(DEFAULT_PRODUCT_LINE)))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productTypeId").value(DEFAULT_PRODUCT_TYPE_ID))
            .andExpect(jsonPath("$.providerId").value(DEFAULT_PROVIDER_ID))
            .andExpect(jsonPath("$.photoId").value(DEFAULT_PHOTO_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.sellPrice").value(DEFAULT_SELL_PRICE.intValue()))
            .andExpect(jsonPath("$.importPrice").value(DEFAULT_IMPORT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.productLine").value(DEFAULT_PRODUCT_LINE))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productTypeId(UPDATED_PRODUCT_TYPE_ID)
            .providerId(UPDATED_PROVIDER_ID)
            .photoId(UPDATED_PHOTO_ID)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .sellPrice(UPDATED_SELL_PRICE)
            .importPrice(UPDATED_IMPORT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .productLine(UPDATED_PRODUCT_LINE)
            .salePrice(UPDATED_POINT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductTypeId()).isEqualTo(UPDATED_PRODUCT_TYPE_ID);
        assertThat(testProduct.getProviderId()).isEqualTo(UPDATED_PROVIDER_ID);
        assertThat(testProduct.getPhotoId()).isEqualTo(UPDATED_PHOTO_ID);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getSellPrice()).isEqualTo(UPDATED_SELL_PRICE);
        assertThat(testProduct.getImportPrice()).isEqualTo(UPDATED_IMPORT_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProduct.getProductLine()).isEqualTo(UPDATED_PRODUCT_LINE);
        assertThat(testProduct.getSalePrice()).isEqualTo(UPDATED_POINT);
        assertThat(testProduct.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
