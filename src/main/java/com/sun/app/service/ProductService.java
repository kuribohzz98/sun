package com.sun.app.service;

import com.sun.app.domain.Product;
import com.sun.app.repository.ProductRepository;
import com.sun.app.service.dto.ProductDTO;
import com.sun.app.service.mapper.ProductMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    private final ProductMapper productMapper;

    @Autowired
    ResourceLoader resourceLoader;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(
        Pageable pageable,
        Integer salePrice,
        Integer productTypeId,
        Integer providerId,
        Long maxSellPrice,
        Long minSellPrice
    ) {
        log.debug("Request to get all Products");
        Page<Product> page;
        if (productTypeId != null) {
            if (providerId != null) {
                if (maxSellPrice != null && minSellPrice != null) {
                    page = productRepository.findAllByProductTypeIdAndProviderIdAndSellPriceBetween(
                        productTypeId,
                        providerId,
                        minSellPrice,
                        maxSellPrice,
                        pageable
                    );
                } else {
                    page = productRepository.findAllByProductTypeIdAndProviderId(
                        productTypeId,
                        providerId,
                        pageable
                    );
                }
            } else {
                if (maxSellPrice != null && minSellPrice != null) {
                    page = productRepository.findAllByProductTypeIdAndSellPriceBetween(
                        productTypeId,
                        minSellPrice,
                        maxSellPrice,
                        pageable
                    );
                } else {
                    page = productRepository.findAllByProductTypeId(productTypeId, pageable);
                }
            }
        } else if (salePrice != null) {
            page = productRepository.findAllBySalePriceGreaterThan(salePrice, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }
        return page.map(productMapper::toDto).map(productDTO -> {
                InputStream in = ProductService.class.getClassLoader()
                    .getResourceAsStream("assest/upload/"+ productDTO.getImage());
                if(in == null) return productDTO;
                byte[] media = new byte[0];
                try {
                    media = IOUtils.toByteArray(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                productDTO.setImage(Base64.encode(media));
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return productDTO;
            });
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOneByCode(String code) {
        return productRepository.findOneByCode(code).map(productMapper::toDto)
            .map(productDTO -> {
                InputStream in = ProductService.class.getClassLoader()
                    .getResourceAsStream("assest/upload/"+ productDTO.getImage());
                if(in == null) return productDTO;
                byte[] media = new byte[0];
                try {
                    media = IOUtils.toByteArray(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                productDTO.setImage(Base64.encode(media));
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return productDTO;
            });
    }


    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto)
            .map(productDTO -> {
            InputStream in = ProductService.class.getClassLoader()
                .getResourceAsStream("assest/upload/"+ productDTO.getImage());
            if(in == null) return productDTO;
            byte[] media = new byte[0];
            try {
                media = IOUtils.toByteArray(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            productDTO.setImage(Base64.encode(media));
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return productDTO;
        });
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
