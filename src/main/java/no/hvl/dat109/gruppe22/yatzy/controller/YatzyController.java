package no.hvl.dat109.gruppe22.yatzy.controller;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import no.hvl.dat109.gruppe22.yatzy.service.BrukerService;
import no.hvl.dat109.gruppe22.yatzy.service.SpillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class YatzyController {

    @Autowired
    private BrukerRepository brukerRepo;

    @Autowired
    private BrukerService brukerService;

    @Autowired
    private SpillRepository spillRepo;

    @Autowired
    private SpillService spillService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("brukere", brukerRepo.findAll());
        model.addAttribute("spill", spillRepo.findAll());
        return "home";
    }

    @PostMapping("/nybruker")
    public String nyBruker(@RequestParam String brukernavn) {
        Bruker bruker = new Bruker();
        bruker.setBrukernavn(brukernavn);
        brukerRepo.save(bruker);
        return "redirect:/";
    }

    @PostMapping("/nyttspill")
    public String nyttSpill() {
        Spill spill = new Spill();
        spill.setStartet(false);
        spillRepo.save(spill);
        return "redirect:/";
    }

    @GetMapping("/spill/{id}")
    public String administrerSpill(@PathVariable Long id, Model model) {
        Spill spill = spillRepo.findById(id).orElse(null);

        if (spill == null) {
            return "redirect:/feil";
        }

        Set<Bruker> deltakere = spill.getDeltakere();
        List<Bruker> andreBrukere = brukerService.finnBrukereIkkePameldtSpill(spill);

        model.addAttribute("spill", spill);
        model.addAttribute("deltakere", deltakere);
        model.addAttribute("andreBrukere", andreBrukere);
        return "administrerspill";
    }

    @GetMapping("/spill/{spillId}/leggtildeltaker/{brukerId}")
    public String leggTilDeltaker(@PathVariable Long spillId, @PathVariable Long brukerId) {
        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker deltaker = brukerRepo.findById(brukerId).orElse(null);

        spillService.leggTilDeltaker(spill, deltaker);
        return "redirect:/spill/{spillId}";
    }

    @GetMapping("/spill/{spillId}/start")
    public String startSpill(@PathVariable Long spillId) {
        Spill spill = spillRepo.findById(spillId).orElse(null);

        if ((spill == null) || spill.getDeltakere().isEmpty()) {
            return "redirect:/feil";
        }

        spill.setStartet(true);
        spillRepo.save(spill);
        return "redirect:/spill/{spillId}";
    }
}
