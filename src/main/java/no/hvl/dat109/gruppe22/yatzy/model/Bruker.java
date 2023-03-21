package no.hvl.dat109.gruppe22.yatzy.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "yatzy")
public class Bruker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brukernavn;

    @ManyToMany(mappedBy = "deltakere")
    private Set<Spill> spill;

    public Set<Spill> getSpill() {
        return spill;
    }

    public void setSpill(Set<Spill> spill) {
        this.spill = spill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bruker bruker = (Bruker) o;
        return Objects.equals(id, bruker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void leggTilSpill(Spill s) {
        if (this.spill == null) {
            this.spill = new HashSet<>();
        }

        this.spill.add(s);
    }
}
