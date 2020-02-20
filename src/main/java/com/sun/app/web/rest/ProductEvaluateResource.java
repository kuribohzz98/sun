package com.sun.app.web.rest;

import com.sun.app.domain.ProductEvaluate;
import com.sun.app.service.ProductEvaluateService;
import com.sun.app.web.rest.errors.BadRequestAlertException;
import com.sun.app.service.dto.ProductEvaluateDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sun.app.domain.ProductEvaluate}.
 */
@RestController
@RequestMapping("/api")
public class ProductEvaluateResource {

    private final Logger log = LoggerFactory.getLogger(ProductEvaluateResource.class);

    private static final String ENTITY_NAME = "productEvaluate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductEvaluateService productEvaluateService;

    public ProductEvaluateResource(ProductEvaluateService productEvaluateService) {
        this.productEvaluateService = productEvaluateService;
    }

    /**
     * {@code POST  /product-evaluates} : Create a new productEvaluate.
     *
     * @param productEvaluateDTO the productEvaluateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productEvaluateDTO, or with status {@code 400 (Bad Request)} if the productEvaluate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-evaluates")
    public ResponseEntity<ProductEvaluateDTO> createProductEvaluate(@Valid @RequestBody ProductEvaluateDTO productEvaluateDTO) throws URISyntaxException {
        log.debug("REST request to save ProductEvaluate : {}", productEvaluateDTO);
        if (productEvaluateDTO.getId() != null) {
            throw new BadRequestAlertException("A new productEvaluate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductEvaluateDTO result = productEvaluateService.save(productEvaluateDTO);
        return ResponseEntity.created(new URI("/api/product-evaluates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-evaluates} : Updates an existing productEvaluate.
     *
     * @param productEvaluateDTO the productEvaluateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productEvaluateDTO,
     * or with status {@code 400 (Bad Request)} if the productEvaluateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productEvaluateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-evaluates")
    public ResponseEntity<ProductEvaluateDTO> updateProductEvaluate(@Valid @RequestBody ProductEvaluateDTO productEvaluateDTO) throws URISyntaxException {
        log.debug("REST request to update ProductEvaluate : {}", productEvaluateDTO);
        if (productEvaluateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductEvaluateDTO result = productEvaluateService.save(productEvaluateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productEvaluateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-evaluates} : get all the productEvaluates.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productEvaluates in body.
     */
    @GetMapping("/product-evaluates")
    public ResponseEntity<List<ProductEvaluate>> getAllProductEvaluates(Pageable pageable) {
        log.debug("REST request to get a page of ProductEvaluates");
        Page<ProductEvaluate> page = productEvaluateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-evaluates/:id} : get the "id" productEvaluate.
     *
     * @param id the id of the productEvaluateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productEvaluateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-evaluates/{id}")
    public ResponseEntity<ProductEvaluateDTO> getProductEvaluate(@PathVariable Long id) {
        log.debug("REST request to get ProductEvaluate : {}", id);
        Optional<ProductEvaluateDTO> productEvaluateDTO = productEvaluateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productEvaluateDTO);
    }

    /**
     * {@code DELETE  /product-evaluates/:id} : delete the "id" productEvaluate.
     *
     * @param id the id of the productEvaluateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-evaluates/{id}")
    public ResponseEntity<Void> deleteProductEvaluate(@PathVariable Long id) {
        log.debug("REST request to delete ProductEvaluate : {}", id);
        productEvaluateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/product-evaluates/get")
    public ResponseEntity<List<ProductEvaluate>> getProductEvaluateByOptions(@RequestParam Long productId, Pageable pageable) {
        Page<ProductEvaluate> page = productEvaluateService.findAllByProductId(productId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
