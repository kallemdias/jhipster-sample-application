package dev.mellak.pigeonal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PigeonMapperTest {

    private PigeonMapper pigeonMapper;

    @BeforeEach
    public void setUp() {
        pigeonMapper = new PigeonMapperImpl();
    }
}
