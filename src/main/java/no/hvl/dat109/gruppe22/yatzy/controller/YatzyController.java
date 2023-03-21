package no.hvl.dat109.gruppe22.yatzy.controller;

import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
