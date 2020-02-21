package com.sun.app.service;

import com.sun.app.domain.Payment;
import com.sun.app.domain.Product;
import com.sun.app.repository.PaymentRepository;
import com.sun.app.repository.ProductRepository;
import com.sun.app.service.dto.PaymentDTO;
import com.sun.app.service.dto.ProductDTO;
import com.sun.app.service.mapper.PaymentMapper;
import com.sun.app.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, ProductRepository productRepository, ProductMapper productMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Set<ProductDTO> productListDto = paymentDTO.getProducts();
        for (ProductDTO productDto: productListDto) {
            Optional<Product> product = productRepository.findById(productDto.getId());
            product.map(productMapper::toDto).map(productDTO -> {
                productDTO.setQuantity(productDTO.getQuantity() - productDto.getQuantity());
                productRepository.save(productMapper.toEntity(productDTO));
                return productDTO;
            });
        }

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }

    /**
     * Get all the payments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PaymentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentRepository.findAllWithEagerRelationships(pageable).map(paymentMapper::toDto);
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findOneWithEagerRelationships(id)
            .map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
