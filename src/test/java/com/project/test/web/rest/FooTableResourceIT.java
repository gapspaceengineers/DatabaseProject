package com.project.test.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.project.test.IntegrationTest;
import com.project.test.domain.FooTable;
import com.project.test.repository.FooTableRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FooTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FooTableResourceIT {

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_LIST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_LIST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_DATE = "AAAAAAAAAA";
    private static final String UPDATED_FROM_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_TO_DATE = "AAAAAAAAAA";
    private static final String UPDATED_TO_DATE = "BBBBBBBBBB";

    private static final Long DEFAULT_SORTING_PRIORITY = 1L;
    private static final Long UPDATED_SORTING_PRIORITY = 2L;

    private static final String ENTITY_API_URL = "/api/foo-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{code}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FooTableRepository fooTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFooTableMockMvc;

    private FooTable fooTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FooTable createEntity(EntityManager em) {
        FooTable fooTable = new FooTable()
            .source(DEFAULT_SOURCE)
            .codeListCode(DEFAULT_CODE_LIST_CODE)
            .displayValue(DEFAULT_DISPLAY_VALUE)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .sortingPriority(DEFAULT_SORTING_PRIORITY);
        return fooTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FooTable createUpdatedEntity(EntityManager em) {
        FooTable fooTable = new FooTable()
            .source(UPDATED_SOURCE)
            .codeListCode(UPDATED_CODE_LIST_CODE)
            .displayValue(UPDATED_DISPLAY_VALUE)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .sortingPriority(UPDATED_SORTING_PRIORITY);
        return fooTable;
    }

    @BeforeEach
    public void initTest() {
        fooTable = createEntity(em);
    }

    @Test
    @Transactional
    void createFooTable() throws Exception {
        int databaseSizeBeforeCreate = fooTableRepository.findAll().size();
        // Create the FooTable
        restFooTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fooTable)))
            .andExpect(status().isCreated());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeCreate + 1);
        FooTable testFooTable = fooTableList.get(fooTableList.size() - 1);
        assertThat(testFooTable.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testFooTable.getCodeListCode()).isEqualTo(DEFAULT_CODE_LIST_CODE);
        assertThat(testFooTable.getDisplayValue()).isEqualTo(DEFAULT_DISPLAY_VALUE);
        assertThat(testFooTable.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testFooTable.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testFooTable.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testFooTable.getSortingPriority()).isEqualTo(DEFAULT_SORTING_PRIORITY);
    }

    @Test
    @Transactional
    void createFooTableWithExistingId() throws Exception {
        // Create the FooTable with an existing ID
        fooTable.setCode(1L);

        int databaseSizeBeforeCreate = fooTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFooTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fooTable)))
            .andExpect(status().isBadRequest());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFooTables() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        // Get all the fooTableList
        restFooTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=code,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].code").value(hasItem(fooTable.getCode().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].codeListCode").value(hasItem(DEFAULT_CODE_LIST_CODE)))
            .andExpect(jsonPath("$.[*].displayValue").value(hasItem(DEFAULT_DISPLAY_VALUE)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE)))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE)))
            .andExpect(jsonPath("$.[*].sortingPriority").value(hasItem(DEFAULT_SORTING_PRIORITY.intValue())));
    }

    @Test
    @Transactional
    void getFooTable() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        // Get the fooTable
        restFooTableMockMvc
            .perform(get(ENTITY_API_URL_ID, fooTable.getCode()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.code").value(fooTable.getCode().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.codeListCode").value(DEFAULT_CODE_LIST_CODE))
            .andExpect(jsonPath("$.displayValue").value(DEFAULT_DISPLAY_VALUE))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE))
            .andExpect(jsonPath("$.sortingPriority").value(DEFAULT_SORTING_PRIORITY.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFooTable() throws Exception {
        // Get the fooTable
        restFooTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFooTable() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();

        // Update the fooTable
        FooTable updatedFooTable = fooTableRepository.findById(fooTable.getCode()).get();
        // Disconnect from session so that the updates on updatedFooTable are not directly saved in db
        em.detach(updatedFooTable);
        updatedFooTable
            .source(UPDATED_SOURCE)
            .codeListCode(UPDATED_CODE_LIST_CODE)
            .displayValue(UPDATED_DISPLAY_VALUE)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .sortingPriority(UPDATED_SORTING_PRIORITY);

        restFooTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFooTable.getCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFooTable))
            )
            .andExpect(status().isOk());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
        FooTable testFooTable = fooTableList.get(fooTableList.size() - 1);
        assertThat(testFooTable.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testFooTable.getCodeListCode()).isEqualTo(UPDATED_CODE_LIST_CODE);
        assertThat(testFooTable.getDisplayValue()).isEqualTo(UPDATED_DISPLAY_VALUE);
        assertThat(testFooTable.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testFooTable.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testFooTable.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testFooTable.getSortingPriority()).isEqualTo(UPDATED_SORTING_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fooTable.getCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fooTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fooTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fooTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFooTableWithPatch() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();

        // Update the fooTable using partial update
        FooTable partialUpdatedFooTable = new FooTable();
        partialUpdatedFooTable.setCode(fooTable.getCode());

        partialUpdatedFooTable.source(UPDATED_SOURCE).codeListCode(UPDATED_CODE_LIST_CODE).sortingPriority(UPDATED_SORTING_PRIORITY);

        restFooTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFooTable.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFooTable))
            )
            .andExpect(status().isOk());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
        FooTable testFooTable = fooTableList.get(fooTableList.size() - 1);
        assertThat(testFooTable.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testFooTable.getCodeListCode()).isEqualTo(UPDATED_CODE_LIST_CODE);
        assertThat(testFooTable.getDisplayValue()).isEqualTo(DEFAULT_DISPLAY_VALUE);
        assertThat(testFooTable.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testFooTable.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testFooTable.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testFooTable.getSortingPriority()).isEqualTo(UPDATED_SORTING_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateFooTableWithPatch() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();

        // Update the fooTable using partial update
        FooTable partialUpdatedFooTable = new FooTable();
        partialUpdatedFooTable.setCode(fooTable.getCode());

        partialUpdatedFooTable
            .source(UPDATED_SOURCE)
            .codeListCode(UPDATED_CODE_LIST_CODE)
            .displayValue(UPDATED_DISPLAY_VALUE)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .sortingPriority(UPDATED_SORTING_PRIORITY);

        restFooTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFooTable.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFooTable))
            )
            .andExpect(status().isOk());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
        FooTable testFooTable = fooTableList.get(fooTableList.size() - 1);
        assertThat(testFooTable.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testFooTable.getCodeListCode()).isEqualTo(UPDATED_CODE_LIST_CODE);
        assertThat(testFooTable.getDisplayValue()).isEqualTo(UPDATED_DISPLAY_VALUE);
        assertThat(testFooTable.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testFooTable.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testFooTable.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testFooTable.getSortingPriority()).isEqualTo(UPDATED_SORTING_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fooTable.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fooTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fooTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFooTable() throws Exception {
        int databaseSizeBeforeUpdate = fooTableRepository.findAll().size();
        fooTable.setCode(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFooTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fooTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FooTable in the database
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFooTable() throws Exception {
        // Initialize the database
        fooTableRepository.saveAndFlush(fooTable);

        int databaseSizeBeforeDelete = fooTableRepository.findAll().size();

        // Delete the fooTable
        restFooTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, fooTable.getCode()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FooTable> fooTableList = fooTableRepository.findAll();
        assertThat(fooTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
