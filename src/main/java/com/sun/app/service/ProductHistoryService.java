package com.sun.app.service;

import com.sun.app.domain.ProductHistory;
import com.sun.app.repository.ProductHistoryRepository;
import com.sun.app.service.dto.ProductHistoryDTO;
import com.sun.app.service.mapper.ProductHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductHistory}.
 */
@Service
@Transactional
public class ProductHistoryService {

    private final Logger log = LoggerFactory.getLogger(ProductHistoryService.class);

    private final ProductHistoryRepository productHistoryRepository;

    private final ProductHistoryMapper productHistoryMapper;

    public ProductHistoryService(ProductHistoryRepository productHistoryRepository, ProductHistoryMapper productHistoryMapper) {
        this.productHistoryRepository = productHistoryRepository;
        this.productHistoryMapper = productHistoryMapper;
    }

    /**
     * Save a productHistory.
     *
     * @param productHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductHistoryDTO save(ProductHistoryDTO productHistoryDTO) {
        log.debug("Request to save ProductHistory : {}", productHistoryDTO);
        ProductHistory productHistory = productHistoryMapper.toEntity(productHistoryDTO);
        productHistory = productHistoryRepository.save(productHistory);
        return productHistoryMapper.toDto(productHistory);
    }

    /**
     * Get all the productHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductHistories");
        return productHistoryRepository.findAll(pageable)
            .map(productHistoryMapper::toDto);
    }


    /**
     * Get one productHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductHistoryDTO> findOne(Long id) {
        log.debug("Request to get ProductHistory : {}", id);
        return productHistoryRepository.findById(id)
            .map(productHistoryMapper::toDto);
    }

    /**
     * Delete the productHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductHistory : {}", id);
        productHistoryRepository.deleteById(id);
    }
}
