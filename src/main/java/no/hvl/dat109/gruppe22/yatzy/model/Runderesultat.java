package no.hvl.dat109.gruppe22.yatzy.model;

import jakarta.persistence.*;
import no.hvl.dat109.gruppe22.yatzy.spill.TerningUtil;

import java.util.Map;
import java.util.Set;

@Entity
@Table(schema = "yatzy")
public class Runderesultat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int kombinasjonstype;
    private int kastnr;

    @ManyToOne
    @JoinColumn(name = "bruker_id")
    private Bruker bruker;

    @ManyToOne
    @JoinColumn(name = "spill_id")
    private Spill spill;

    public Runderesultat() {}

    public Runderesultat(Map<String, Integer> terninger, int kombinasjonstype, int kastnr, Bruker bruker, Spill spill) {
        t1 = terninger.get("t1");
        t2 = terninger.get("t2");
        t3 = terninger.get("t3");
        t4 = terninger.get("t4");
        t5 = terninger.get("t5");
        this.kombinasjonstype = kombinasjonstype;
        this.kastnr = kastnr;
        this.bruker = bruker;
        this.spill = spill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getT1() {
        return t1;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public int getT2() {
        return t2;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getT4() {
        return t4;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getT5() {
        return t5;
    }

    public void setT5(int t5) {
        this.t5 = t5;
    }

    public int getKombinasjonstype() {
        return kombinasjonstype;
    }

    public void setKombinasjonstype(int kombinasjonstype) {
        this.kombinasjonstype = kombinasjonstype;
    }

    public int getKastnr() {
        return kastnr;
    }

    public void setKastnr(int kastnr) {
        this.kastnr = kastnr;
    }

    public Bruker getBruker() {
        return bruker;
    }

    public void setBruker(Bruker bruker) {
        this.bruker = bruker;
    }

    public Spill getSpill() {
        return spill;
    }

    public void setSpill(Spill spill) {
        this.spill = spill;
    }

    @Override
    public String toString() {
        return "Runderesultat{" +
                "id=" + id +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                ", t5=" + t5 +
                ", kombinasjonstype=" + kombinasjonstype +
                ", kastnr=" + kastnr +
                ", bruker=" + bruker.getBrukernavn() +
                ", spill=" + spill.getId() +
                '}';
    }

    public Map<String, Integer> hentTerningverdier() {
        return Map.of(
                "t1", t1,
                "t2", t2,
                "t3", t3,
                "t4", t4,
                "t5", t5
        );
    }

    public void trillTerninger(Set<String> behold) {
        if (behold == null || !behold.contains("t1")) {
            t1 = TerningUtil.terningkast();
        }

        if (behold == null || !behold.contains("t2")) {
            t2 = TerningUtil.terningkast();
        }

        if (behold == null || !behold.contains("t3")) {
            t3 = TerningUtil.terningkast();
        }

        if (behold == null || !behold.contains("t4")) {
            t4 = TerningUtil.terningkast();
        }

        if (behold == null || !behold.contains("t5")) {
            t5 = TerningUtil.terningkast();
        }

        kastnr++;
    }
}
