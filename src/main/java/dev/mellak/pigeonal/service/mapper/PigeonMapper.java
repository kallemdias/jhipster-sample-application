package dev.mellak.pigeonal.service.mapper;

import dev.mellak.pigeonal.domain.Pigeon;
import dev.mellak.pigeonal.service.dto.PigeonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pigeon} and its DTO {@link PigeonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PigeonMapper extends EntityMapper<PigeonDTO, Pigeon> {
    @Mapping(target = "mother", source = "mother", qualifiedByName = "pigeonId")
    @Mapping(target = "father", source = "father", qualifiedByName = "pigeonId")
    PigeonDTO toDto(Pigeon s);

    @Named("pigeonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PigeonDTO toDtoPigeonId(Pigeon pigeon);
}
