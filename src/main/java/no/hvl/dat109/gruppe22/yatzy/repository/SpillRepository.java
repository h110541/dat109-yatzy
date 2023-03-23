package no.hvl.dat109.gruppe22.yatzy.repository;

import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpillRepository extends CrudRepository<Spill, Long> {
    @Query("SELECT s FROM Spill s ORDER BY s.id")
    List<Spill> alleSortertPaId();
}
