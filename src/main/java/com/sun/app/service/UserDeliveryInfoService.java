package com.sun.app.service;

import com.sun.app.domain.UserDeliveryInfo;
import com.sun.app.repository.UserDeliveryInfoRepository;
import com.sun.app.service.dto.UserDeliveryInfoDTO;
import com.sun.app.service.mapper.UserDeliveryInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UserDeliveryInfo}.
 */
@Service
@Transactional
public class UserDeliveryInfoService {

    private final Logger log = LoggerFactory.getLogger(UserDeliveryInfoService.class);

    private final UserDeliveryInfoRepository userDeliveryInfoRepository;

    private final UserDeliveryInfoMapper userDeliveryInfoMapper;

    public UserDeliveryInfoService(UserDeliveryInfoRepository userDeliveryInfoRepository, UserDeliveryInfoMapper userDeliveryInfoMapper) {
        this.userDeliveryInfoRepository = userDeliveryInfoRepository;
        this.userDeliveryInfoMapper = userDeliveryInfoMapper;
    }

    /**
     * Save a userDeliveryInfo.
     *
     * @param userDeliveryInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDeliveryInfoDTO save(UserDeliveryInfoDTO userDeliveryInfoDTO) {
        log.debug("Request to save UserDeliveryInfo : {}", userDeliveryInfoDTO);
        UserDeliveryInfo userDeliveryInfo = userDeliveryInfoMapper.toEntity(userDeliveryInfoDTO);
        userDeliveryInfo = userDeliveryInfoRepository.save(userDeliveryInfo);
        return userDeliveryInfoMapper.toDto(userDeliveryInfo);
    }

    /**
     * Get all the userDeliveryInfos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserDeliveryInfoDTO> findAll() {
        log.debug("Request to get all UserDeliveryInfos");
        return userDeliveryInfoRepository.findAll().stream()
            .map(userDeliveryInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one userDeliveryInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDeliveryInfoDTO> findOne(Long id) {
        log.debug("Request to get UserDeliveryInfo : {}", id);
        return userDeliveryInfoRepository.findById(id)
            .map(userDeliveryInfoMapper::toDto);
    }

    /**
     * Delete the userDeliveryInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserDeliveryInfo : {}", id);
        userDeliveryInfoRepository.deleteById(id);
    }
}
