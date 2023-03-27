package no.hvl.dat109.gruppe22.yatzy.controller;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Runderesultat;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;
import no.hvl.dat109.gruppe22.yatzy.repository.BrukerRepository;
import no.hvl.dat109.gruppe22.yatzy.repository.RunderesultatRepository;
import no.hvl.dat109.gruppe22.yatzy.repository.SpillRepository;
import no.hvl.dat109.gruppe22.yatzy.service.BrukerService;
import no.hvl.dat109.gruppe22.yatzy.service.RunderesultatService;
import no.hvl.dat109.gruppe22.yatzy.service.SpillService;
import no.hvl.dat109.gruppe22.yatzy.spill.Kombinasjonstyper;
import no.hvl.dat109.gruppe22.yatzy.spill.Poengberegning;
import no.hvl.dat109.gruppe22.yatzy.spill.TerningUtil;
import no.hvl.dat109.gruppe22.yatzy.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

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

    @Autowired
    private RunderesultatRepository runderesultatRepo;

    @Autowired
    private RunderesultatService runderesultatService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean loggetInn = (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        if (loggetInn) {
            return "meny";
        }

        return "homeikkeloggetinn";
    }

    @PostMapping("/spill/opprettnyttspill")
    public String nyttSpill(Principal principal) {
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        Spill spill = new Spill(bruker);
        spill = spillRepo.save(spill);

        return "redirect:/spill/vis/" + spill.getId();
    }

    @GetMapping("/spill/mine")
    public String visMineSpill(Principal principal, Model model) {
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);
        List<Spill> spill = spillRepo.findByDeltakereAndAvsluttetFalseOrderById(bruker);
        List<Map<String, String>> spillinfo = new ArrayList<>(spill.size());

        for (Spill s : spill) {
            spillinfo.add(
                    Map.of(
                            "id", s.getId().toString(),
                            "status", s.status(),
                            "opprettetAv", s.getOpprettetAv().getBrukernavn()
                    )
            );
        }

        model.addAttribute("spill", spillinfo);
        return "spilloversikt";
    }

    @GetMapping("/spill/ledige")
    public String visLedigeSpill(Principal principal, Model model) {
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);
        List<Spill> spill = spillService.finnSpillMedLedigePlasser(bruker);
        List<Map<String, String>> spillinfo = new ArrayList<>(spill.size());

        for (Spill s : spill) {
            spillinfo.add(
                    Map.of(
                            "id", s.getId().toString(),
                            "status", s.status(),
                            "opprettetAv", s.getOpprettetAv().getBrukernavn()
                    )
            );
        }

        model.addAttribute("spill", spillinfo);
        return "spilloversikt";
    }

    @GetMapping("/spill/vis/{id}")
    public String visSpill(@PathVariable Long id, Principal principal, Model model) {
        Spill spill = spillRepo.findById(id).orElse(null);
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        if (spill == null) {
            return "redirect:/feil";
        }

        if (!spill.getStartet() && !spill.getDeltakere().contains(bruker)) {
            model.addAttribute("visMeldpaa", true);
        } else if (!spill.getStartet() && spill.getOpprettetAv().equals(bruker)) {
            model.addAttribute("visStart", true);
        } else if (spill.getStartet() && spill.getDeltakere().contains(bruker)) {
            model.addAttribute("visSpillnaa", true);
        }

        model.addAttribute("spill", spill);
        return "visspill";
    }

    @PostMapping("/spill/meldpaa/{spillId}")
    public String meldPaa(@PathVariable Long spillId, Principal principal) {
        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        spillService.leggTilDeltaker(spill, bruker);
        return "redirect:/spill/vis/{spillId}";
    }

    @PostMapping("/spill/start/{spillId}")
    public String startSpill(@PathVariable Long spillId, Principal principal) {
        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        if ((spill == null) || spill.getDeltakere().isEmpty() || !spill.getOpprettetAv().equals(bruker)) {
            return "redirect:/feil";
        }

        spill.setStartet(true);
        spillRepo.save(spill);
        return "redirect:/spill/vis/{spillId}";
    }

    @GetMapping("/spill/spill/{spillId}")
    public String spillYatzy(@PathVariable Long spillId, Principal principal, Model model, RedirectAttributes ra,
                             @RequestParam(required = false) Set<String> checked,
                             @RequestParam(required = false) Long bekreftresultatid) {

        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        String feilmelding = Util.sjekkGyldigSpillOgBruker(spill, spillId, bruker);
        if (feilmelding != null) {
            ra.addFlashAttribute("feilmelding", feilmelding);
            return "redirect:/feil";
        }

        Runderesultat aktivKombinasjon = runderesultatService.finnAktiv(spill, bruker).orElse(null);
        Map<Integer, Runderesultat> resultater = runderesultatService.resultaterSpillBruker(spill, bruker);

        List<Bruker> deltakere = brukerRepo.findBySpillOrderById(spill);
        List<Map<String, Integer>> poengMapListe = spillService.genererPoengMapListe(spill, deltakere);
        model.addAttribute("deltakere", deltakere);
        model.addAttribute("pliste", poengMapListe);

        Integer forrigeKombinasjonstype = runderesultatService.forrigeKombinasjonstype(spill, bruker);
        int kombinasjonstype = (forrigeKombinasjonstype == null) ? 1 : forrigeKombinasjonstype + 1;

        if (aktivKombinasjon == null && bekreftresultatid != null) {
            Runderesultat rr = runderesultatRepo.findById(bekreftresultatid).orElse(null);

            int resultat = Poengberegning.beregnRunde(rr);
            kombinasjonstype = rr.getKombinasjonstype();

            model.addAttribute("kastnr", 3);
            model.addAttribute("resultat", resultat);
            model.addAttribute("terninger", TerningUtil.terningVerdierTilSymboler(rr.hentTerningverdier()));
            model.addAttribute("side", "kast3");
        } else if (resultater.size() == Kombinasjonstyper.ANTALL_KOMBINASJONSTYPER) {
            int brukerIndex = deltakere.indexOf(bruker);
            model.addAttribute("side", "ferdig");
            model.addAttribute("brukerPoengTotalt", poengMapListe.get(brukerIndex).get("totalt"));
        } else if (aktivKombinasjon == null) {
            model.addAttribute("terninger", TerningUtil.terningVerdierTilSymboler(TerningUtil.KUN_ENERE));
            model.addAttribute("side", "nyttkast");
        } else {
            int kastnr = aktivKombinasjon.getKastnr();
            model.addAttribute("kastnr", kastnr);
            model.addAttribute("terninger", TerningUtil.terningVerdierTilSymboler(aktivKombinasjon.hentTerningverdier()));
            model.addAttribute("side", "kast" + kastnr); // kast 1 eller 2
        }

        model.addAttribute("kombinasjonstype", Kombinasjonstyper.NAVN_UI.get(kombinasjonstype));
        model.addAttribute("spillId", spillId);
        model.addAttribute("brukerId", bruker.getId());

        if (checked != null) {
            model.addAttribute("checked", checked);
        }

        return "yatzy";
    }

    @PostMapping ("/spill/spill/{spillId}")
    public String spillYatzyPost(@PathVariable Long spillId, Principal principal, Model model, RedirectAttributes ra,
                                 @RequestParam(required = false) Set<String> behold) {

        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findByBrukernavn(principal.getName()).orElse(null);

        String feilmelding = Util.sjekkGyldigSpillOgBruker(spill, spillId, bruker);
        if (feilmelding != null) {
            ra.addFlashAttribute("feilmelding", feilmelding);
            return "redirect:/feil";
        }

        Runderesultat aktivKombinasjon = runderesultatService.finnAktiv(spill, bruker).orElse(null);

        if (aktivKombinasjon == null) {
            Map<Integer, Runderesultat> resultater = runderesultatService.resultaterSpillBruker(spill, bruker);

            if (resultater.size() < Kombinasjonstyper.ANTALL_KOMBINASJONSTYPER) {
                Map<String, Integer> terninger = TerningUtil.kastAlle();
                Runderesultat nyRr = new Runderesultat(terninger, Kombinasjonstyper.AKTIV, 1, bruker, spill);
                runderesultatRepo.save(nyRr);
            }
        } else if (aktivKombinasjon.getKastnr() == 1) {
            aktivKombinasjon.trillTerninger(behold);
            runderesultatRepo.save(aktivKombinasjon);
        } else if (aktivKombinasjon.getKastnr() == 2) {
            aktivKombinasjon.trillTerninger(behold);

            boolean resultatErYatzy = false;
            if (Poengberegning.beregnYatzy(aktivKombinasjon.hentTerningverdier().values()) != 0) {
                Map<Integer, Runderesultat> resultater = runderesultatService.resultaterSpillBruker(spill, bruker);

                if (!resultater.containsKey(Kombinasjonstyper.YATZY)) {
                    resultatErYatzy = true;
                    aktivKombinasjon.setKombinasjonstype(Kombinasjonstyper.YATZY);
                }
            }

            if (!resultatErYatzy) {
                Integer forrigeKombinasjonstype = runderesultatService.forrigeKombinasjonstype(spill, bruker);
                int kombinasjonstype = (forrigeKombinasjonstype == null) ? 1 : forrigeKombinasjonstype + 1;
                aktivKombinasjon.setKombinasjonstype(kombinasjonstype);
            }

            behold = null;
            ra.addAttribute("bekreftresultatid", aktivKombinasjon.getId());
            runderesultatRepo.save(aktivKombinasjon);
        }

        if (behold != null) {
            ra.addAttribute("checked", behold);
        }

        return "redirect:/spill/spill/{spillId}";
    }

    @GetMapping("/feil")
    public String feil() {
        return "feil";
    }
}
