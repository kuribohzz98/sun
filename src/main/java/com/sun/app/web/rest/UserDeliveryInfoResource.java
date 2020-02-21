package com.sun.app.web.rest;

import com.sun.app.service.UserDeliveryInfoService;
import com.sun.app.web.rest.errors.BadRequestAlertException;
import com.sun.app.service.dto.UserDeliveryInfoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sun.app.domain.UserDeliveryInfo}.
 */
@RestController
@RequestMapping("/api")
public class UserDeliveryInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserDeliveryInfoResource.class);

    private static final String ENTITY_NAME = "userDeliveryInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDeliveryInfoService userDeliveryInfoService;

    public UserDeliveryInfoResource(UserDeliveryInfoService userDeliveryInfoService) {
        this.userDeliveryInfoService = userDeliveryInfoService;
    }

    /**
     * {@code POST  /user-delivery-infos} : Create a new userDeliveryInfo.
     *
     * @param userDeliveryInfoDTO the userDeliveryInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDeliveryInfoDTO, or with status {@code 400 (Bad Request)} if the userDeliveryInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-delivery-infos")
    public ResponseEntity<UserDeliveryInfoDTO> createUserDeliveryInfo(@Valid @RequestBody UserDeliveryInfoDTO userDeliveryInfoDTO) throws URISyntaxException {
        log.debug("REST request to save UserDeliveryInfo : {}", userDeliveryInfoDTO);
        if (userDeliveryInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDeliveryInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDeliveryInfoDTO result = userDeliveryInfoService.save(userDeliveryInfoDTO);
        return ResponseEntity.created(new URI("/api/user-delivery-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-delivery-infos} : Updates an existing userDeliveryInfo.
     *
     * @param userDeliveryInfoDTO the userDeliveryInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDeliveryInfoDTO,
     * or with status {@code 400 (Bad Request)} if the userDeliveryInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDeliveryInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-delivery-infos")
    public ResponseEntity<UserDeliveryInfoDTO> updateUserDeliveryInfo(@Valid @RequestBody UserDeliveryInfoDTO userDeliveryInfoDTO) throws URISyntaxException {
        log.debug("REST request to update UserDeliveryInfo : {}", userDeliveryInfoDTO);
        if (userDeliveryInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserDeliveryInfoDTO result = userDeliveryInfoService.save(userDeliveryInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDeliveryInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-delivery-infos} : get all the userDeliveryInfos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDeliveryInfos in body.
     */
    @GetMapping("/user-delivery-infos")
    public List<UserDeliveryInfoDTO> getAllUserDeliveryInfos() {
        log.debug("REST request to get all UserDeliveryInfos");
        return userDeliveryInfoService.findAll();
    }

    /**
     * {@code GET  /user-delivery-infos/:id} : get the "id" userDeliveryInfo.
     *
     * @param id the id of the userDeliveryInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDeliveryInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-delivery-infos/{id}")
    public ResponseEntity<UserDeliveryInfoDTO> getUserDeliveryInfo(@PathVariable Long id) {
        log.debug("REST request to get UserDeliveryInfo : {}", id);
        Optional<UserDeliveryInfoDTO> userDeliveryInfoDTO = userDeliveryInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDeliveryInfoDTO);
    }

    /**
     * {@code DELETE  /user-delivery-infos/:id} : delete the "id" userDeliveryInfo.
     *
     * @param id the id of the userDeliveryInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-delivery-infos/{id}")
    public ResponseEntity<Void> deleteUserDeliveryInfo(@PathVariable Long id) {
        log.debug("REST request to delete UserDeliveryInfo : {}", id);
        userDeliveryInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
