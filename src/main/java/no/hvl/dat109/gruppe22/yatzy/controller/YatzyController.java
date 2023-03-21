package no.hvl.dat109.gruppe22.yatzy.controller;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class YatzyController {

    @Autowired
    private BrukerRepository brukerRepo;

    @Autowired
    private SpillRepository spillRepo;

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
}
