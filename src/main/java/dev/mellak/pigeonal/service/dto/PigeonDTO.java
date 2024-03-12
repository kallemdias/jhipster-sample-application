package dev.mellak.pigeonal.service.dto;

import dev.mellak.pigeonal.domain.enumeration.ColorPattern;
import dev.mellak.pigeonal.domain.enumeration.Gender;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.mellak.pigeonal.domain.Pigeon} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PigeonDTO implements Serializable {

    private Long id;

    @NotNull
    private String ringNumber;

    private String name;

    @NotNull
    private String breeder;

    private Gender gender;

    private Integer birthYear;

    private ColorPattern colorPattern;

    @Lob
    private String longDescription;

    private String mediumDescription;

    private String shortDescription;

    private PigeonDTO mother;

    private PigeonDTO father;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRingNumber() {
        return ringNumber;
    }

    public void setRingNumber(String ringNumber) {
        this.ringNumber = ringNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreeder() {
        return breeder;
    }

    public void setBreeder(String breeder) {
        this.breeder = breeder;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public ColorPattern getColorPattern() {
        return colorPattern;
    }

    public void setColorPattern(ColorPattern colorPattern) {
        this.colorPattern = colorPattern;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getMediumDescription() {
        return mediumDescription;
    }

    public void setMediumDescription(String mediumDescription) {
        this.mediumDescription = mediumDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public PigeonDTO getMother() {
        return mother;
    }

    public void setMother(PigeonDTO mother) {
        this.mother = mother;
    }

    public PigeonDTO getFather() {
        return father;
    }

    public void setFather(PigeonDTO father) {
        this.father = father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PigeonDTO)) {
            return false;
        }

        PigeonDTO pigeonDTO = (PigeonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pigeonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PigeonDTO{" +
            "id=" + getId() +
            ", ringNumber='" + getRingNumber() + "'" +
            ", name='" + getName() + "'" +
            ", breeder='" + getBreeder() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthYear=" + getBirthYear() +
            ", colorPattern='" + getColorPattern() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", mediumDescription='" + getMediumDescription() + "'" +
            ", shortDescription='" + getShortDescription() + "'" +
            ", mother=" + getMother() +
            ", father=" + getFather() +
            "}";
    }
}
