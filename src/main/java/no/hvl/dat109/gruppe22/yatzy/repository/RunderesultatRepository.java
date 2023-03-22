package no.hvl.dat109.gruppe22.yatzy.repository;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Runderesultat;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RunderesultatRepository extends CrudRepository<Runderesultat, Long> {

    Optional<Runderesultat> findBySpillAndBrukerAndKombinasjonstype(Spill spill, Bruker bruker, int kombinasjonstype);

    List<Runderesultat> findBySpillAndBrukerAndKombinasjonstypeNot(Spill spill, Bruker bruker, int kombinasjonstype);

    List<Runderesultat> findBySpillAndKombinasjonstypeNot(Spill spill, int kombinasjonstype);

    @Query("SELECT MAX(rr.kombinasjonstype) FROM Runderesultat rr WHERE rr.spill = :spill AND rr.bruker = :bruker AND rr.kombinasjonstype != :aktiv AND rr.kombinasjonstype != :yatzy")
    Integer forrigeKombinasjonstype(Spill spill, Bruker bruker, int aktiv, int yatzy);
}
