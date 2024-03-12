package dev.mellak.pigeonal.service;

import dev.mellak.pigeonal.domain.Pigeon;
import dev.mellak.pigeonal.repository.PigeonRepository;
import dev.mellak.pigeonal.service.dto.PigeonDTO;
import dev.mellak.pigeonal.service.mapper.PigeonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dev.mellak.pigeonal.domain.Pigeon}.
 */
@Service
@Transactional
public class PigeonService {

    private final Logger log = LoggerFactory.getLogger(PigeonService.class);

    private final PigeonRepository pigeonRepository;

    private final PigeonMapper pigeonMapper;

    public PigeonService(PigeonRepository pigeonRepository, PigeonMapper pigeonMapper) {
        this.pigeonRepository = pigeonRepository;
        this.pigeonMapper = pigeonMapper;
    }

    /**
     * Save a pigeon.
     *
     * @param pigeonDTO the entity to save.
     * @return the persisted entity.
     */
    public PigeonDTO save(PigeonDTO pigeonDTO) {
        log.debug("Request to save Pigeon : {}", pigeonDTO);
        Pigeon pigeon = pigeonMapper.toEntity(pigeonDTO);
        pigeon = pigeonRepository.save(pigeon);
        return pigeonMapper.toDto(pigeon);
    }

    /**
     * Update a pigeon.
     *
     * @param pigeonDTO the entity to save.
     * @return the persisted entity.
     */
    public PigeonDTO update(PigeonDTO pigeonDTO) {
        log.debug("Request to update Pigeon : {}", pigeonDTO);
        Pigeon pigeon = pigeonMapper.toEntity(pigeonDTO);
        pigeon = pigeonRepository.save(pigeon);
        return pigeonMapper.toDto(pigeon);
    }

    /**
     * Partially update a pigeon.
     *
     * @param pigeonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PigeonDTO> partialUpdate(PigeonDTO pigeonDTO) {
        log.debug("Request to partially update Pigeon : {}", pigeonDTO);

        return pigeonRepository
            .findById(pigeonDTO.getId())
            .map(existingPigeon -> {
                pigeonMapper.partialUpdate(existingPigeon, pigeonDTO);

                return existingPigeon;
            })
            .map(pigeonRepository::save)
            .map(pigeonMapper::toDto);
    }

    /**
     * Get all the pigeons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PigeonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pigeons");
        return pigeonRepository.findAll(pageable).map(pigeonMapper::toDto);
    }

    /**
     * Get one pigeon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PigeonDTO> findOne(Long id) {
        log.debug("Request to get Pigeon : {}", id);
        return pigeonRepository.findById(id).map(pigeonMapper::toDto);
    }

    /**
     * Delete the pigeon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pigeon : {}", id);
        pigeonRepository.deleteById(id);
    }
}
