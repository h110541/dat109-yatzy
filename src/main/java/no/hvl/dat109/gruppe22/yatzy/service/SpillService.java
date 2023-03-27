package no.hvl.dat109.gruppe22.yatzy.service;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Runderesultat;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import no.hvl.dat109.gruppe22.yatzy.spill.Kombinasjonstyper;
import no.hvl.dat109.gruppe22.yatzy.spill.Poengberegning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SpillService {
    @Autowired
    private SpillRepository spillRepo;
    @Autowired
    private RunderesultatService runderesultatService;

    public void leggTilDeltaker(Spill spill, Bruker deltaker) {
        if (spill != null && deltaker != null && !spill.getStartet() && (spill.getDeltakere().size() < 6)) {
            spill.leggTilDeltaker(deltaker);

            if (spill.getDeltakere().size() == 6) {
                spill.setStartet(true);
            }

            spillRepo.save(spill);
        }
    }

    /**
     * Genererer en liste av poengmaps for hver deltaker (brukes av template), hvor REKKEFØLGEN SAMSVARER MED
     * listen av deltakere.
     *
     * @param spill     spillet det gjelder
     * @param deltakere liste av alle deltakerene i spillet
     * @return          liste av poengmaps hvor hvert element har SAMME INDEX som tilsvarende bruker i deltakere-listen
     */
    public List<Map<String, Integer>> genererPoengMapListe(Spill spill, List<Bruker> deltakere) {
        List<Map<String, Integer>> poengMapListe = new ArrayList<>(deltakere.size());

        for (Bruker bruker : deltakere) {
            Map<Integer, Runderesultat> brukerResultater = runderesultatService.resultaterSpillBruker(spill, bruker);
            poengMapListe.add(Poengberegning.genererPoengMap(brukerResultater, Kombinasjonstyper.NAVN));
        }

        return poengMapListe;
    }

    public List<Spill> finnSpillMedLedigePlasser(Bruker bruker) {
        List<Spill> ikkeStartet = spillRepo.findByStartet(false);

        return ikkeStartet.stream()
                .filter(s -> !s.getDeltakere().contains(bruker))
                .toList();
    }
}
