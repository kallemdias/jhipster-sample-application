package dev.mellak.pigeonal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.mellak.pigeonal.IntegrationTest;
import dev.mellak.pigeonal.domain.Pigeon;
import dev.mellak.pigeonal.domain.enumeration.ColorPattern;
import dev.mellak.pigeonal.domain.enumeration.Gender;
import dev.mellak.pigeonal.repository.PigeonRepository;
import dev.mellak.pigeonal.service.dto.PigeonDTO;
import dev.mellak.pigeonal.service.mapper.PigeonMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PigeonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PigeonResourceIT {

    private static final String DEFAULT_RING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RING_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BREEDER = "AAAAAAAAAA";
    private static final String UPDATED_BREEDER = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Integer DEFAULT_BIRTH_YEAR = 1;
    private static final Integer UPDATED_BIRTH_YEAR = 2;

    private static final ColorPattern DEFAULT_COLOR_PATTERN = ColorPattern.BLUE_BAR;
    private static final ColorPattern UPDATED_COLOR_PATTERN = ColorPattern.BLACK_BAR;

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIUM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pigeons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PigeonRepository pigeonRepository;

    @Autowired
    private PigeonMapper pigeonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPigeonMockMvc;

    private Pigeon pigeon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pigeon createEntity(EntityManager em) {
        Pigeon pigeon = new Pigeon()
            .ringNumber(DEFAULT_RING_NUMBER)
            .name(DEFAULT_NAME)
            .breeder(DEFAULT_BREEDER)
            .gender(DEFAULT_GENDER)
            .birthYear(DEFAULT_BIRTH_YEAR)
            .colorPattern(DEFAULT_COLOR_PATTERN)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .mediumDescription(DEFAULT_MEDIUM_DESCRIPTION)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION);
        return pigeon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pigeon createUpdatedEntity(EntityManager em) {
        Pigeon pigeon = new Pigeon()
            .ringNumber(UPDATED_RING_NUMBER)
            .name(UPDATED_NAME)
            .breeder(UPDATED_BREEDER)
            .gender(UPDATED_GENDER)
            .birthYear(UPDATED_BIRTH_YEAR)
            .colorPattern(UPDATED_COLOR_PATTERN)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .mediumDescription(UPDATED_MEDIUM_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION);
        return pigeon;
    }

    @BeforeEach
    public void initTest() {
        pigeon = createEntity(em);
    }

    @Test
    @Transactional
    void createPigeon() throws Exception {
        int databaseSizeBeforeCreate = pigeonRepository.findAll().size();
        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);
        restPigeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pigeonDTO)))
            .andExpect(status().isCreated());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeCreate + 1);
        Pigeon testPigeon = pigeonList.get(pigeonList.size() - 1);
        assertThat(testPigeon.getRingNumber()).isEqualTo(DEFAULT_RING_NUMBER);
        assertThat(testPigeon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPigeon.getBreeder()).isEqualTo(DEFAULT_BREEDER);
        assertThat(testPigeon.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPigeon.getBirthYear()).isEqualTo(DEFAULT_BIRTH_YEAR);
        assertThat(testPigeon.getColorPattern()).isEqualTo(DEFAULT_COLOR_PATTERN);
        assertThat(testPigeon.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testPigeon.getMediumDescription()).isEqualTo(DEFAULT_MEDIUM_DESCRIPTION);
        assertThat(testPigeon.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPigeonWithExistingId() throws Exception {
        // Create the Pigeon with an existing ID
        pigeon.setId(1L);
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        int databaseSizeBeforeCreate = pigeonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPigeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pigeonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRingNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = pigeonRepository.findAll().size();
        // set the field null
        pigeon.setRingNumber(null);

        // Create the Pigeon, which fails.
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        restPigeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pigeonDTO)))
            .andExpect(status().isBadRequest());

        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBreederIsRequired() throws Exception {
        int databaseSizeBeforeTest = pigeonRepository.findAll().size();
        // set the field null
        pigeon.setBreeder(null);

        // Create the Pigeon, which fails.
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        restPigeonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pigeonDTO)))
            .andExpect(status().isBadRequest());

        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPigeons() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        // Get all the pigeonList
        restPigeonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pigeon.getId().intValue())))
            .andExpect(jsonPath("$.[*].ringNumber").value(hasItem(DEFAULT_RING_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].breeder").value(hasItem(DEFAULT_BREEDER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthYear").value(hasItem(DEFAULT_BIRTH_YEAR)))
            .andExpect(jsonPath("$.[*].colorPattern").value(hasItem(DEFAULT_COLOR_PATTERN.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mediumDescription").value(hasItem(DEFAULT_MEDIUM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPigeon() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        // Get the pigeon
        restPigeonMockMvc
            .perform(get(ENTITY_API_URL_ID, pigeon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pigeon.getId().intValue()))
            .andExpect(jsonPath("$.ringNumber").value(DEFAULT_RING_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.breeder").value(DEFAULT_BREEDER))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthYear").value(DEFAULT_BIRTH_YEAR))
            .andExpect(jsonPath("$.colorPattern").value(DEFAULT_COLOR_PATTERN.toString()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mediumDescription").value(DEFAULT_MEDIUM_DESCRIPTION))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPigeon() throws Exception {
        // Get the pigeon
        restPigeonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPigeon() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();

        // Update the pigeon
        Pigeon updatedPigeon = pigeonRepository.findById(pigeon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPigeon are not directly saved in db
        em.detach(updatedPigeon);
        updatedPigeon
            .ringNumber(UPDATED_RING_NUMBER)
            .name(UPDATED_NAME)
            .breeder(UPDATED_BREEDER)
            .gender(UPDATED_GENDER)
            .birthYear(UPDATED_BIRTH_YEAR)
            .colorPattern(UPDATED_COLOR_PATTERN)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .mediumDescription(UPDATED_MEDIUM_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION);
        PigeonDTO pigeonDTO = pigeonMapper.toDto(updatedPigeon);

        restPigeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pigeonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
        Pigeon testPigeon = pigeonList.get(pigeonList.size() - 1);
        assertThat(testPigeon.getRingNumber()).isEqualTo(UPDATED_RING_NUMBER);
        assertThat(testPigeon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPigeon.getBreeder()).isEqualTo(UPDATED_BREEDER);
        assertThat(testPigeon.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPigeon.getBirthYear()).isEqualTo(UPDATED_BIRTH_YEAR);
        assertThat(testPigeon.getColorPattern()).isEqualTo(UPDATED_COLOR_PATTERN);
        assertThat(testPigeon.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testPigeon.getMediumDescription()).isEqualTo(UPDATED_MEDIUM_DESCRIPTION);
        assertThat(testPigeon.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pigeonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pigeonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePigeonWithPatch() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();

        // Update the pigeon using partial update
        Pigeon partialUpdatedPigeon = new Pigeon();
        partialUpdatedPigeon.setId(pigeon.getId());

        partialUpdatedPigeon.colorPattern(UPDATED_COLOR_PATTERN);

        restPigeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPigeon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPigeon))
            )
            .andExpect(status().isOk());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
        Pigeon testPigeon = pigeonList.get(pigeonList.size() - 1);
        assertThat(testPigeon.getRingNumber()).isEqualTo(DEFAULT_RING_NUMBER);
        assertThat(testPigeon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPigeon.getBreeder()).isEqualTo(DEFAULT_BREEDER);
        assertThat(testPigeon.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPigeon.getBirthYear()).isEqualTo(DEFAULT_BIRTH_YEAR);
        assertThat(testPigeon.getColorPattern()).isEqualTo(UPDATED_COLOR_PATTERN);
        assertThat(testPigeon.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testPigeon.getMediumDescription()).isEqualTo(DEFAULT_MEDIUM_DESCRIPTION);
        assertThat(testPigeon.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePigeonWithPatch() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();

        // Update the pigeon using partial update
        Pigeon partialUpdatedPigeon = new Pigeon();
        partialUpdatedPigeon.setId(pigeon.getId());

        partialUpdatedPigeon
            .ringNumber(UPDATED_RING_NUMBER)
            .name(UPDATED_NAME)
            .breeder(UPDATED_BREEDER)
            .gender(UPDATED_GENDER)
            .birthYear(UPDATED_BIRTH_YEAR)
            .colorPattern(UPDATED_COLOR_PATTERN)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .mediumDescription(UPDATED_MEDIUM_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION);

        restPigeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPigeon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPigeon))
            )
            .andExpect(status().isOk());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
        Pigeon testPigeon = pigeonList.get(pigeonList.size() - 1);
        assertThat(testPigeon.getRingNumber()).isEqualTo(UPDATED_RING_NUMBER);
        assertThat(testPigeon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPigeon.getBreeder()).isEqualTo(UPDATED_BREEDER);
        assertThat(testPigeon.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPigeon.getBirthYear()).isEqualTo(UPDATED_BIRTH_YEAR);
        assertThat(testPigeon.getColorPattern()).isEqualTo(UPDATED_COLOR_PATTERN);
        assertThat(testPigeon.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testPigeon.getMediumDescription()).isEqualTo(UPDATED_MEDIUM_DESCRIPTION);
        assertThat(testPigeon.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pigeonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPigeon() throws Exception {
        int databaseSizeBeforeUpdate = pigeonRepository.findAll().size();
        pigeon.setId(longCount.incrementAndGet());

        // Create the Pigeon
        PigeonDTO pigeonDTO = pigeonMapper.toDto(pigeon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPigeonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pigeonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pigeon in the database
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePigeon() throws Exception {
        // Initialize the database
        pigeonRepository.saveAndFlush(pigeon);

        int databaseSizeBeforeDelete = pigeonRepository.findAll().size();

        // Delete the pigeon
        restPigeonMockMvc
            .perform(delete(ENTITY_API_URL_ID, pigeon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pigeon> pigeonList = pigeonRepository.findAll();
        assertThat(pigeonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
