package dev.mellak.pigeonal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.mellak.pigeonal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PigeonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PigeonDTO.class);
        PigeonDTO pigeonDTO1 = new PigeonDTO();
        pigeonDTO1.setId(1L);
        PigeonDTO pigeonDTO2 = new PigeonDTO();
        assertThat(pigeonDTO1).isNotEqualTo(pigeonDTO2);
        pigeonDTO2.setId(pigeonDTO1.getId());
        assertThat(pigeonDTO1).isEqualTo(pigeonDTO2);
        pigeonDTO2.setId(2L);
        assertThat(pigeonDTO1).isNotEqualTo(pigeonDTO2);
        pigeonDTO1.setId(null);
        assertThat(pigeonDTO1).isNotEqualTo(pigeonDTO2);
    }
}
