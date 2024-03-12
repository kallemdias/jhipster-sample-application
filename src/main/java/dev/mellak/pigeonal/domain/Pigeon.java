package dev.mellak.pigeonal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.mellak.pigeonal.domain.enumeration.ColorPattern;
import dev.mellak.pigeonal.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pigeon.
 */
@Entity
@Table(name = "pigeon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pigeon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ring_number", nullable = false)
    private String ringNumber;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "breeder", nullable = false)
    private String breeder;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_pattern")
    private ColorPattern colorPattern;

    @Lob
    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "medium_description")
    private String mediumDescription;

    @Column(name = "short_description")
    private String shortDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mother", "father" }, allowSetters = true)
    private Pigeon mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mother", "father" }, allowSetters = true)
    private Pigeon father;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pigeon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRingNumber() {
        return this.ringNumber;
    }

    public Pigeon ringNumber(String ringNumber) {
        this.setRingNumber(ringNumber);
        return this;
    }

    public void setRingNumber(String ringNumber) {
        this.ringNumber = ringNumber;
    }

    public String getName() {
        return this.name;
    }

    public Pigeon name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreeder() {
        return this.breeder;
    }

    public Pigeon breeder(String breeder) {
        this.setBreeder(breeder);
        return this;
    }

    public void setBreeder(String breeder) {
        this.breeder = breeder;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Pigeon gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getBirthYear() {
        return this.birthYear;
    }

    public Pigeon birthYear(Integer birthYear) {
        this.setBirthYear(birthYear);
        return this;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public ColorPattern getColorPattern() {
        return this.colorPattern;
    }

    public Pigeon colorPattern(ColorPattern colorPattern) {
        this.setColorPattern(colorPattern);
        return this;
    }

    public void setColorPattern(ColorPattern colorPattern) {
        this.colorPattern = colorPattern;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public Pigeon longDescription(String longDescription) {
        this.setLongDescription(longDescription);
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getMediumDescription() {
        return this.mediumDescription;
    }

    public Pigeon mediumDescription(String mediumDescription) {
        this.setMediumDescription(mediumDescription);
        return this;
    }

    public void setMediumDescription(String mediumDescription) {
        this.mediumDescription = mediumDescription;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public Pigeon shortDescription(String shortDescription) {
        this.setShortDescription(shortDescription);
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Pigeon getMother() {
        return this.mother;
    }

    public void setMother(Pigeon pigeon) {
        this.mother = pigeon;
    }

    public Pigeon mother(Pigeon pigeon) {
        this.setMother(pigeon);
        return this;
    }

    public Pigeon getFather() {
        return this.father;
    }

    public void setFather(Pigeon pigeon) {
        this.father = pigeon;
    }

    public Pigeon father(Pigeon pigeon) {
        this.setFather(pigeon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pigeon)) {
            return false;
        }
        return getId() != null && getId().equals(((Pigeon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pigeon{" +
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
            "}";
    }
}
