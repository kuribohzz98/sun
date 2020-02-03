package com.sun.app.service;

import com.sun.app.domain.ProductEvaluate;
import com.sun.app.repository.ProductEvaluateRepository;
import com.sun.app.service.dto.ProductEvaluateDTO;
import com.sun.app.service.mapper.ProductEvaluateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductEvaluate}.
 */
@Service
@Transactional
public class ProductEvaluateService {

    private final Logger log = LoggerFactory.getLogger(ProductEvaluateService.class);

    private final ProductEvaluateRepository productEvaluateRepository;

    private final ProductEvaluateMapper productEvaluateMapper;

    public ProductEvaluateService(ProductEvaluateRepository productEvaluateRepository, ProductEvaluateMapper productEvaluateMapper) {
        this.productEvaluateRepository = productEvaluateRepository;
        this.productEvaluateMapper = productEvaluateMapper;
    }

    /**
     * Save a productEvaluate.
     *
     * @param productEvaluateDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductEvaluateDTO save(ProductEvaluateDTO productEvaluateDTO) {
        log.debug("Request to save ProductEvaluate : {}", productEvaluateDTO);
        ProductEvaluate productEvaluate = productEvaluateMapper.toEntity(productEvaluateDTO);
        productEvaluate = productEvaluateRepository.save(productEvaluate);
        return productEvaluateMapper.toDto(productEvaluate);
    }

    /**
     * Get all the productEvaluates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductEvaluateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductEvaluates");
        return productEvaluateRepository.findAll(pageable)
            .map(productEvaluateMapper::toDto);
    }


    /**
     * Get one productEvaluate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductEvaluateDTO> findOne(Long id) {
        log.debug("Request to get ProductEvaluate : {}", id);
        return productEvaluateRepository.findById(id)
            .map(productEvaluateMapper::toDto);
    }

    /**
     * Delete the productEvaluate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductEvaluate : {}", id);
        productEvaluateRepository.deleteById(id);
    }
}
