package no.hvl.dat109.gruppe22.yatzy.service;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpillService {
    @Autowired
    private SpillRepository spillRepo;

    public void leggTilDeltaker(Spill spill, Bruker deltaker) {
        if (spill != null && deltaker != null && !spill.getStartet() && (spill.getDeltakere().size() < 6)) {
            spill.leggTilDeltaker(deltaker);
            spillRepo.save(spill);
        }
    }
}
