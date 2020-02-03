package com.sun.app.web.rest;

import com.sun.app.SunApp;
import com.sun.app.domain.UserDeliveryInfo;
import com.sun.app.domain.User;
import com.sun.app.repository.UserDeliveryInfoRepository;
import com.sun.app.service.UserDeliveryInfoService;
import com.sun.app.service.dto.UserDeliveryInfoDTO;
import com.sun.app.service.mapper.UserDeliveryInfoMapper;
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
import java.util.List;

import static com.sun.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserDeliveryInfoResource} REST controller.
 */
@SpringBootTest(classes = SunApp.class)
public class UserDeliveryInfoResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_WARD = "AAAAAAAAAA";
    private static final String UPDATED_WARD = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private UserDeliveryInfoRepository userDeliveryInfoRepository;

    @Autowired
    private UserDeliveryInfoMapper userDeliveryInfoMapper;

    @Autowired
    private UserDeliveryInfoService userDeliveryInfoService;

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

    private MockMvc restUserDeliveryInfoMockMvc;

    private UserDeliveryInfo userDeliveryInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserDeliveryInfoResource userDeliveryInfoResource = new UserDeliveryInfoResource(userDeliveryInfoService);
        this.restUserDeliveryInfoMockMvc = MockMvcBuilders.standaloneSetup(userDeliveryInfoResource)
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
    public static UserDeliveryInfo createEntity(EntityManager em) {
        UserDeliveryInfo userDeliveryInfo = new UserDeliveryInfo()
            .userId(DEFAULT_USER_ID)
            .phone(DEFAULT_PHONE)
            .city(DEFAULT_CITY)
            .district(DEFAULT_DISTRICT)
            .ward(DEFAULT_WARD)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDeliveryInfo.setUser(user);
        return userDeliveryInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDeliveryInfo createUpdatedEntity(EntityManager em) {
        UserDeliveryInfo userDeliveryInfo = new UserDeliveryInfo()
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .district(UPDATED_DISTRICT)
            .ward(UPDATED_WARD)
            .address(UPDATED_ADDRESS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDeliveryInfo.setUser(user);
        return userDeliveryInfo;
    }

    @BeforeEach
    public void initTest() {
        userDeliveryInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDeliveryInfo() throws Exception {
        int databaseSizeBeforeCreate = userDeliveryInfoRepository.findAll().size();

        // Create the UserDeliveryInfo
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);
        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the UserDeliveryInfo in the database
        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserDeliveryInfo testUserDeliveryInfo = userDeliveryInfoList.get(userDeliveryInfoList.size() - 1);
        assertThat(testUserDeliveryInfo.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserDeliveryInfo.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserDeliveryInfo.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserDeliveryInfo.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testUserDeliveryInfo.getWard()).isEqualTo(DEFAULT_WARD);
        assertThat(testUserDeliveryInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createUserDeliveryInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDeliveryInfoRepository.findAll().size();

        // Create the UserDeliveryInfo with an existing ID
        userDeliveryInfo.setId(1L);
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDeliveryInfo in the database
        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDeliveryInfoRepository.findAll().size();
        // set the field null
        userDeliveryInfo.setUserId(null);

        // Create the UserDeliveryInfo, which fails.
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDeliveryInfoRepository.findAll().size();
        // set the field null
        userDeliveryInfo.setPhone(null);

        // Create the UserDeliveryInfo, which fails.
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDeliveryInfoRepository.findAll().size();
        // set the field null
        userDeliveryInfo.setCity(null);

        // Create the UserDeliveryInfo, which fails.
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistrictIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDeliveryInfoRepository.findAll().size();
        // set the field null
        userDeliveryInfo.setDistrict(null);

        // Create the UserDeliveryInfo, which fails.
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWardIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDeliveryInfoRepository.findAll().size();
        // set the field null
        userDeliveryInfo.setWard(null);

        // Create the UserDeliveryInfo, which fails.
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(post("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserDeliveryInfos() throws Exception {
        // Initialize the database
        userDeliveryInfoRepository.saveAndFlush(userDeliveryInfo);

        // Get all the userDeliveryInfoList
        restUserDeliveryInfoMockMvc.perform(get("/api/user-delivery-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDeliveryInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].ward").value(hasItem(DEFAULT_WARD)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getUserDeliveryInfo() throws Exception {
        // Initialize the database
        userDeliveryInfoRepository.saveAndFlush(userDeliveryInfo);

        // Get the userDeliveryInfo
        restUserDeliveryInfoMockMvc.perform(get("/api/user-delivery-infos/{id}", userDeliveryInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userDeliveryInfo.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.ward").value(DEFAULT_WARD))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    public void getNonExistingUserDeliveryInfo() throws Exception {
        // Get the userDeliveryInfo
        restUserDeliveryInfoMockMvc.perform(get("/api/user-delivery-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDeliveryInfo() throws Exception {
        // Initialize the database
        userDeliveryInfoRepository.saveAndFlush(userDeliveryInfo);

        int databaseSizeBeforeUpdate = userDeliveryInfoRepository.findAll().size();

        // Update the userDeliveryInfo
        UserDeliveryInfo updatedUserDeliveryInfo = userDeliveryInfoRepository.findById(userDeliveryInfo.getId()).get();
        // Disconnect from session so that the updates on updatedUserDeliveryInfo are not directly saved in db
        em.detach(updatedUserDeliveryInfo);
        updatedUserDeliveryInfo
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .district(UPDATED_DISTRICT)
            .ward(UPDATED_WARD)
            .address(UPDATED_ADDRESS);
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(updatedUserDeliveryInfo);

        restUserDeliveryInfoMockMvc.perform(put("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isOk());

        // Validate the UserDeliveryInfo in the database
        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeUpdate);
        UserDeliveryInfo testUserDeliveryInfo = userDeliveryInfoList.get(userDeliveryInfoList.size() - 1);
        assertThat(testUserDeliveryInfo.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserDeliveryInfo.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserDeliveryInfo.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserDeliveryInfo.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testUserDeliveryInfo.getWard()).isEqualTo(UPDATED_WARD);
        assertThat(testUserDeliveryInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDeliveryInfo() throws Exception {
        int databaseSizeBeforeUpdate = userDeliveryInfoRepository.findAll().size();

        // Create the UserDeliveryInfo
        UserDeliveryInfoDTO userDeliveryInfoDTO = userDeliveryInfoMapper.toDto(userDeliveryInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDeliveryInfoMockMvc.perform(put("/api/user-delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDeliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDeliveryInfo in the database
        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserDeliveryInfo() throws Exception {
        // Initialize the database
        userDeliveryInfoRepository.saveAndFlush(userDeliveryInfo);

        int databaseSizeBeforeDelete = userDeliveryInfoRepository.findAll().size();

        // Delete the userDeliveryInfo
        restUserDeliveryInfoMockMvc.perform(delete("/api/user-delivery-infos/{id}", userDeliveryInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDeliveryInfo> userDeliveryInfoList = userDeliveryInfoRepository.findAll();
        assertThat(userDeliveryInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
