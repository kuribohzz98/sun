package com.sun.app.web.rest;

import com.sun.app.service.SpecificationsService;
import com.sun.app.web.rest.errors.BadRequestAlertException;
import com.sun.app.service.dto.SpecificationsDTO;

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
 * REST controller for managing {@link com.sun.app.domain.Specifications}.
 */
@RestController
@RequestMapping("/api")
public class SpecificationsResource {

    private final Logger log = LoggerFactory.getLogger(SpecificationsResource.class);

    private static final String ENTITY_NAME = "specifications";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecificationsService specificationsService;

    public SpecificationsResource(SpecificationsService specificationsService) {
        this.specificationsService = specificationsService;
    }

    /**
     * {@code POST  /specifications} : Create a new specifications.
     *
     * @param specificationsDTO the specificationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specificationsDTO, or with status {@code 400 (Bad Request)} if the specifications has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specifications")
    public ResponseEntity<SpecificationsDTO> createSpecifications(@Valid @RequestBody SpecificationsDTO specificationsDTO) throws URISyntaxException {
        log.debug("REST request to save Specifications : {}", specificationsDTO);
        if (specificationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new specifications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecificationsDTO result = specificationsService.save(specificationsDTO);
        return ResponseEntity.created(new URI("/api/specifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specifications} : Updates an existing specifications.
     *
     * @param specificationsDTO the specificationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specificationsDTO,
     * or with status {@code 400 (Bad Request)} if the specificationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specificationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specifications")
    public ResponseEntity<SpecificationsDTO> updateSpecifications(@Valid @RequestBody SpecificationsDTO specificationsDTO) throws URISyntaxException {
        log.debug("REST request to update Specifications : {}", specificationsDTO);
        if (specificationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecificationsDTO result = specificationsService.save(specificationsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specificationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /specifications} : get all the specifications.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specifications in body.
     */
    @GetMapping("/specifications")
    public ResponseEntity<List<SpecificationsDTO>> getAllSpecifications(Pageable pageable) {
        log.debug("REST request to get a page of Specifications");
        Page<SpecificationsDTO> page = specificationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specifications/:id} : get the "id" specifications.
     *
     * @param id the id of the specificationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specificationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specifications/{id}")
    public ResponseEntity<SpecificationsDTO> getSpecifications(@PathVariable Long id) {
        log.debug("REST request to get Specifications : {}", id);
        Optional<SpecificationsDTO> specificationsDTO = specificationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specificationsDTO);
    }

    /**
     * {@code DELETE  /specifications/:id} : delete the "id" specifications.
     *
     * @param id the id of the specificationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specifications/{id}")
    public ResponseEntity<Void> deleteSpecifications(@PathVariable Long id) {
        log.debug("REST request to delete Specifications : {}", id);
        specificationsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
