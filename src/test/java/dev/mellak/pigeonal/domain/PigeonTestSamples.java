package dev.mellak.pigeonal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PigeonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Pigeon getPigeonSample1() {
        return new Pigeon()
            .id(1L)
            .ringNumber("ringNumber1")
            .name("name1")
            .breeder("breeder1")
            .birthYear(1)
            .mediumDescription("mediumDescription1")
            .shortDescription("shortDescription1");
    }

    public static Pigeon getPigeonSample2() {
        return new Pigeon()
            .id(2L)
            .ringNumber("ringNumber2")
            .name("name2")
            .breeder("breeder2")
            .birthYear(2)
            .mediumDescription("mediumDescription2")
            .shortDescription("shortDescription2");
    }

    public static Pigeon getPigeonRandomSampleGenerator() {
        return new Pigeon()
            .id(longCount.incrementAndGet())
            .ringNumber(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .breeder(UUID.randomUUID().toString())
            .birthYear(intCount.incrementAndGet())
            .mediumDescription(UUID.randomUUID().toString())
            .shortDescription(UUID.randomUUID().toString());
    }
}
