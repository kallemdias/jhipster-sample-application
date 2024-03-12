package dev.mellak.pigeonal.domain;

import static dev.mellak.pigeonal.domain.PigeonTestSamples.*;
import static dev.mellak.pigeonal.domain.PigeonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.mellak.pigeonal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PigeonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pigeon.class);
        Pigeon pigeon1 = getPigeonSample1();
        Pigeon pigeon2 = new Pigeon();
        assertThat(pigeon1).isNotEqualTo(pigeon2);

        pigeon2.setId(pigeon1.getId());
        assertThat(pigeon1).isEqualTo(pigeon2);

        pigeon2 = getPigeonSample2();
        assertThat(pigeon1).isNotEqualTo(pigeon2);
    }

    @Test
    void motherTest() throws Exception {
        Pigeon pigeon = getPigeonRandomSampleGenerator();
        Pigeon pigeonBack = getPigeonRandomSampleGenerator();

        pigeon.setMother(pigeonBack);
        assertThat(pigeon.getMother()).isEqualTo(pigeonBack);

        pigeon.mother(null);
        assertThat(pigeon.getMother()).isNull();
    }

    @Test
    void fatherTest() throws Exception {
        Pigeon pigeon = getPigeonRandomSampleGenerator();
        Pigeon pigeonBack = getPigeonRandomSampleGenerator();

        pigeon.setFather(pigeonBack);
        assertThat(pigeon.getFather()).isEqualTo(pigeonBack);

        pigeon.father(null);
        assertThat(pigeon.getFather()).isNull();
    }
}
