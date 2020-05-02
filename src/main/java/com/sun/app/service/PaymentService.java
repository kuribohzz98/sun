package com.sun.app.service;

import com.sun.app.domain.Payment;
import com.sun.app.domain.Product;
import com.sun.app.domain.enumeration.PaymentStatus;
import com.sun.app.repository.PaymentRepository;
import com.sun.app.repository.ProductRepository;
import com.sun.app.service.dto.PaymentDTO;
import com.sun.app.service.dto.ProductDTO;
import com.sun.app.service.mapper.PaymentMapper;
import com.sun.app.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    private final MomoService momoService;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, ProductRepository productRepository, ProductMapper productMapper, @Lazy MomoService momoService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.momoService = momoService;
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
        List<Product> productList = new ArrayList<>();
        StringBuilder description = new StringBuilder();
        description.append("{");
        for (ProductDTO productDto: productListDto) {
            Optional<Product> product = productRepository.findById(productDto.getId());
            product.map(productMapper::toDto).map(productDTO -> {
                if (productDTO.getQuantity() - productDto.getQuantity() >= 0) {
                    productDTO.setQuantity(productDTO.getQuantity() - productDto.getQuantity());
                    Product product1 = productRepository.save(productMapper.toEntity(productDTO));
                    productList.add(product1);
                    description.append("\"").append(productDto.getId()).append("\"")
                        .append(":").append("\"").append(productDto.getQuantity()).append("\"").append(",");
                }
                return productDTO;
            });
        }
        description.append("}");
        paymentDTO.setDescription(description.toString());
        paymentDTO.setProducts(new HashSet<>());
        paymentDTO.setStatus(PaymentStatus.UNPAID);
        String uuid = UUID.randomUUID().toString();
        paymentDTO.setQrcode(momoService.createQrCode(paymentDTO.getPrice(), uuid));
        paymentDTO.setTransactionId(uuid);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        for (Product product: productList) {
            payment.getProducts().add(product);
        }
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

    public PaymentDTO update(Payment payment) {
        Payment paymentEntity = paymentRepository.save(payment);
        return paymentMapper.toDto(paymentEntity);
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

    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOneByTransactionId(String transactionId) {
        log.debug("Request to get findOneByTransactionId : {}", transactionId);
        return paymentRepository.findOneByTransactionIdWithEagerRelationships(transactionId)
            .map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<List<PaymentDTO>> finAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId).map(paymentMapper::toDto);
    }

    public Integer updateStatusByTransactionId(PaymentStatus status, byte[] qrcode, String transactionId) {
        return paymentRepository.updateStatusAndQrcodeByTransactionId(status, qrcode, transactionId);
    }
}
