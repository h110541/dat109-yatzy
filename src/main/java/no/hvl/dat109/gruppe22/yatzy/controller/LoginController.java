package no.hvl.dat109.gruppe22.yatzy.controller;

import jakarta.validation.Valid;
import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import no.hvl.dat109.gruppe22.yatzy.validation.NyBrukerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder pwenc;
    @Autowired
    private BrukerRepository brukerRepo;

    @GetMapping("/registrerbruker")
    public String registrerBruker(NyBrukerForm nyBrukerForm) {
        return "registrerbruker";
    }

    @PostMapping("/registrerbruker")
    public String registrerBrukerPost(@Valid NyBrukerForm nyBrukerForm, BindingResult bindingResult) {

        if (brukerRepo.existsByBrukernavn(nyBrukerForm.getBrukernavn())) {
            FieldError feil = new FieldError(
                    "nyBrukerForm",
                    "brukernavn",
                    nyBrukerForm.getBrukernavn(),
                    false,
                    null,
                    null,
                    "brukernavnet er registrert fra før"
            );

            bindingResult.addError(feil);
        }

        if (bindingResult.hasErrors()) {
            return "registrerbruker";
        }

        String pwhash = pwenc.encode(nyBrukerForm.getPassord());
        Bruker bruker = new Bruker(nyBrukerForm.getBrukernavn(), nyBrukerForm.getNavn(), nyBrukerForm.getEpost(), pwhash);
        brukerRepo.save(bruker);

        return "redirect:/";
    }
}
