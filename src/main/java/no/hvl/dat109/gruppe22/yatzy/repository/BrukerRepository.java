package no.hvl.dat109.gruppe22.yatzy.repository;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrukerRepository extends CrudRepository<Bruker, Long> {
    @Query("SELECT b FROM Bruker b ORDER BY b.id")
    List<Bruker> alleSortertPaId();

}
