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
    private String navn;
    private String epost;
    private Boolean admin;
    private String pwhash;

    @ManyToMany(mappedBy = "deltakere")
    private Set<Spill> spill;

    public Bruker() {}

    public Bruker(String brukernavn, String navn, String epost, String pwhash) {
        this.brukernavn = brukernavn;
        this.navn = navn;
        this.epost = epost;
        this.pwhash = pwhash;

        admin = false;
    }

    public String[] roller() {
        if (admin) {
            return new String[] { "USER", "ADMIN" };
        }

        return new String[] { "USER" };
    }

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

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getPwhash() {
        return pwhash;
    }

    public void setPwhash(String pwhash) {
        this.pwhash = pwhash;
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
