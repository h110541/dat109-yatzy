package no.hvl.dat109.gruppe22.yatzy.service;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BrukerService {
    @Autowired
    private BrukerRepository brukerRepo;

    public List<Bruker> finnBrukereIkkePameldtSpill(Spill spill) {
        Set<Bruker> deltakere = spill.getDeltakere();
        List<Bruker> andreBrukere = new ArrayList<>();

        brukerRepo.findAll().forEach(bruker -> {
            if (!deltakere.contains(bruker)) {
                andreBrukere.add(bruker);
            }
        });

        return andreBrukere;
    }
}
