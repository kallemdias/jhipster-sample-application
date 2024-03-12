package dev.mellak.pigeonal.web.rest;

import dev.mellak.pigeonal.repository.PigeonRepository;
import dev.mellak.pigeonal.service.PigeonService;
import dev.mellak.pigeonal.service.dto.PigeonDTO;
import dev.mellak.pigeonal.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.mellak.pigeonal.domain.Pigeon}.
 */
@RestController
@RequestMapping("/api/pigeons")
public class PigeonResource {

    private final Logger log = LoggerFactory.getLogger(PigeonResource.class);

    private static final String ENTITY_NAME = "pigeon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PigeonService pigeonService;

    private final PigeonRepository pigeonRepository;

    public PigeonResource(PigeonService pigeonService, PigeonRepository pigeonRepository) {
        this.pigeonService = pigeonService;
        this.pigeonRepository = pigeonRepository;
    }

    /**
     * {@code POST  /pigeons} : Create a new pigeon.
     *
     * @param pigeonDTO the pigeonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pigeonDTO, or with status {@code 400 (Bad Request)} if the pigeon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PigeonDTO> createPigeon(@Valid @RequestBody PigeonDTO pigeonDTO) throws URISyntaxException {
        log.debug("REST request to save Pigeon : {}", pigeonDTO);
        if (pigeonDTO.getId() != null) {
            throw new BadRequestAlertException("A new pigeon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PigeonDTO result = pigeonService.save(pigeonDTO);
        return ResponseEntity
            .created(new URI("/api/pigeons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pigeons/:id} : Updates an existing pigeon.
     *
     * @param id the id of the pigeonDTO to save.
     * @param pigeonDTO the pigeonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pigeonDTO,
     * or with status {@code 400 (Bad Request)} if the pigeonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pigeonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PigeonDTO> updatePigeon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PigeonDTO pigeonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pigeon : {}, {}", id, pigeonDTO);
        if (pigeonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pigeonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pigeonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PigeonDTO result = pigeonService.update(pigeonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pigeonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pigeons/:id} : Partial updates given fields of an existing pigeon, field will ignore if it is null
     *
     * @param id the id of the pigeonDTO to save.
     * @param pigeonDTO the pigeonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pigeonDTO,
     * or with status {@code 400 (Bad Request)} if the pigeonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pigeonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pigeonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PigeonDTO> partialUpdatePigeon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PigeonDTO pigeonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pigeon partially : {}, {}", id, pigeonDTO);
        if (pigeonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pigeonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pigeonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PigeonDTO> result = pigeonService.partialUpdate(pigeonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pigeonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pigeons} : get all the pigeons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pigeons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PigeonDTO>> getAllPigeons(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pigeons");
        Page<PigeonDTO> page = pigeonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pigeons/:id} : get the "id" pigeon.
     *
     * @param id the id of the pigeonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pigeonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PigeonDTO> getPigeon(@PathVariable("id") Long id) {
        log.debug("REST request to get Pigeon : {}", id);
        Optional<PigeonDTO> pigeonDTO = pigeonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pigeonDTO);
    }

    /**
     * {@code DELETE  /pigeons/:id} : delete the "id" pigeon.
     *
     * @param id the id of the pigeonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePigeon(@PathVariable("id") Long id) {
        log.debug("REST request to delete Pigeon : {}", id);
        pigeonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
