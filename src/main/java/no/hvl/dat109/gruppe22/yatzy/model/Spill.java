package no.hvl.dat109.gruppe22.yatzy.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "yatzy")
public class Spill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean startet;

    @ManyToMany
    @JoinTable(
            schema = "yatzy",
            name = "bruker_spill",
            joinColumns = @JoinColumn(name = "spill_id"),
            inverseJoinColumns = @JoinColumn(name = "bruker_id")
    )
    private Set<Bruker> deltakere;

    public Set<Bruker> getDeltakere() {
        return deltakere;
    }

    public void setDeltakere(Set<Bruker> deltakere) {
        this.deltakere = deltakere;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStartet() {
        return startet;
    }

    public void setStartet(Boolean startet) {
        this.startet = startet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spill spill = (Spill) o;
        return Objects.equals(id, spill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void leggTilDeltaker(Bruker deltaker) {
        if (deltakere == null) {
            deltakere = new HashSet<>();
        }

        deltakere.add(deltaker);
    }
}
