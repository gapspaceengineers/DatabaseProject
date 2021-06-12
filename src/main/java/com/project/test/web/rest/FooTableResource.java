package com.project.test.web.rest;

import com.project.test.domain.FooTable;
import com.project.test.repository.FooTableRepository;
import com.project.test.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.project.test.domain.FooTable}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FooTableResource {

    private final Logger log = LoggerFactory.getLogger(FooTableResource.class);

    private static final String ENTITY_NAME = "fooTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FooTableRepository fooTableRepository;

    public FooTableResource(FooTableRepository fooTableRepository) {
        this.fooTableRepository = fooTableRepository;
    }

    /**
     * {@code POST  /foo-tables} : Create a new fooTable.
     *
     * @param fooTable the fooTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fooTable, or with status {@code 400 (Bad Request)} if the fooTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/foo-tables")
    public ResponseEntity<FooTable> createFooTable(@RequestBody FooTable fooTable) throws URISyntaxException {
        log.debug("REST request to save FooTable : {}", fooTable);
        if (fooTable.getCode() != null) {
            throw new BadRequestAlertException("A new fooTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FooTable result = fooTableRepository.save(fooTable);
        return ResponseEntity
            .created(new URI("/api/foo-tables/" + result.getCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getCode().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /foo-tables/:code} : Updates an existing fooTable.
     *
     * @param code the id of the fooTable to save.
     * @param fooTable the fooTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fooTable,
     * or with status {@code 400 (Bad Request)} if the fooTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fooTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/foo-tables/{code}")
    public ResponseEntity<FooTable> updateFooTable(
        @PathVariable(value = "code", required = false) final Long code,
        @RequestBody FooTable fooTable
    ) throws URISyntaxException {
        log.debug("REST request to update FooTable : {}, {}", code, fooTable);
        if (fooTable.getCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(code, fooTable.getCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fooTableRepository.existsById(code)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FooTable result = fooTableRepository.save(fooTable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fooTable.getCode().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /foo-tables/:code} : Partial updates given fields of an existing fooTable, field will ignore if it is null
     *
     * @param code the id of the fooTable to save.
     * @param fooTable the fooTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fooTable,
     * or with status {@code 400 (Bad Request)} if the fooTable is not valid,
     * or with status {@code 404 (Not Found)} if the fooTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the fooTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/foo-tables/{code}", consumes = "application/merge-patch+json")
    public ResponseEntity<FooTable> partialUpdateFooTable(
        @PathVariable(value = "code", required = false) final Long code,
        @RequestBody FooTable fooTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update FooTable partially : {}, {}", code, fooTable);
        if (fooTable.getCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(code, fooTable.getCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fooTableRepository.existsById(code)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FooTable> result = fooTableRepository
            .findById(fooTable.getCode())
            .map(
                existingFooTable -> {
                    if (fooTable.getSource() != null) {
                        existingFooTable.setSource(fooTable.getSource());
                    }
                    if (fooTable.getCodeListCode() != null) {
                        existingFooTable.setCodeListCode(fooTable.getCodeListCode());
                    }
                    if (fooTable.getDisplayValue() != null) {
                        existingFooTable.setDisplayValue(fooTable.getDisplayValue());
                    }
                    if (fooTable.getLongDescription() != null) {
                        existingFooTable.setLongDescription(fooTable.getLongDescription());
                    }
                    if (fooTable.getFromDate() != null) {
                        existingFooTable.setFromDate(fooTable.getFromDate());
                    }
                    if (fooTable.getToDate() != null) {
                        existingFooTable.setToDate(fooTable.getToDate());
                    }
                    if (fooTable.getSortingPriority() != null) {
                        existingFooTable.setSortingPriority(fooTable.getSortingPriority());
                    }

                    return existingFooTable;
                }
            )
            .map(fooTableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fooTable.getCode().toString())
        );
    }

    /**
     * {@code GET  /foo-tables} : get all the fooTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fooTables in body.
     */
    @GetMapping("/foo-tables")
    public List<FooTable> getAllFooTables() {
        log.debug("REST request to get all FooTables");
        return fooTableRepository.findAll();
    }

    /**
     * {@code GET  /foo-tables/:id} : get the "id" fooTable.
     *
     * @param id the id of the fooTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fooTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/foo-tables/{id}")
    public ResponseEntity<FooTable> getFooTable(@PathVariable Long id) {
        log.debug("REST request to get FooTable : {}", id);
        Optional<FooTable> fooTable = fooTableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fooTable);
    }

    /**
     * {@code DELETE  /foo-tables/:id} : delete the "id" fooTable.
     *
     * @param id the id of the fooTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/foo-tables/{id}")
    public ResponseEntity<Void> deleteFooTable(@PathVariable Long id) {
        log.debug("REST request to delete FooTable : {}", id);
        // fooTableRepository.deleteById(id);
        fooTableRepository.deleteAll();
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
