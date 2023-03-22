package no.hvl.dat109.gruppe22.yatzy.service;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Runderesultat;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.RunderesultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static no.hvl.dat109.gruppe22.yatzy.spill.Kombinasjonstyper.AKTIV;
import static no.hvl.dat109.gruppe22.yatzy.spill.Kombinasjonstyper.YATZY;

@Service
public class RunderesultatService {
    @Autowired
    private RunderesultatRepository runderesultatRepo;

    public Optional<Runderesultat> finnAktiv(Spill spill, Bruker bruker) {
        return runderesultatRepo.findBySpillAndBrukerAndKombinasjonstype(spill, bruker, AKTIV);
    }

    public Integer forrigeKombinasjonstype(Spill spill, Bruker bruker) {
        return runderesultatRepo.forrigeKombinasjonstype(spill, bruker, AKTIV, YATZY);
    }

    public Map<Integer, Runderesultat> resultaterSpillBruker(Spill spill, Bruker bruker) {
        List<Runderesultat> liste = runderesultatRepo.findBySpillAndBrukerAndKombinasjonstypeNot(spill, bruker, AKTIV);

        return liste.stream().collect(
                Collectors.toMap(
                        Runderesultat::getKombinasjonstype,
                        Function.identity()
                ));
    }

}
