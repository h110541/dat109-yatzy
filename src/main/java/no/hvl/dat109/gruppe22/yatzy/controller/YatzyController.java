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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private RunderesultatRepository runderesultatRepo;

    @Autowired
    private RunderesultatService runderesultatService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("brukere", brukerRepo.alleSortertPaId());
        model.addAttribute("spill", spillRepo.alleSortertPaId());
        return "home";
    }

    @PostMapping("/nybruker")
    public String nyBruker(@RequestParam String brukernavn) {
        if (!brukernavn.isBlank()) {
            Bruker bruker = new Bruker();
            bruker.setBrukernavn(brukernavn.strip());
            brukerRepo.save(bruker);
        }

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

    @GetMapping("/spill/{spillId}/spillsomdeltaker/{brukerId}")
    public String spillYatzy(@PathVariable Long spillId, @PathVariable Long brukerId, Model model, RedirectAttributes ra,
                             @RequestParam(required = false) Set<String> checked,
                             @RequestParam(required = false) Long bekreftresultatid) {

        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findById(brukerId).orElse(null);

        String feilmelding = Util.sjekkGyldigSpillOgBruker(spill, spillId, bruker, brukerId);
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
        model.addAttribute("brukerId", brukerId);

        if (checked != null) {
            model.addAttribute("checked", checked);
        }

        return "yatzy";
    }

    @PostMapping ("/spill/{spillId}/spillsomdeltaker/{brukerId}")
    public String spillYatzyPost(@PathVariable Long spillId, @PathVariable Long brukerId, Model model, RedirectAttributes ra,
                                 @RequestParam(required = false) Set<String> behold) {

        Spill spill = spillRepo.findById(spillId).orElse(null);
        Bruker bruker = brukerRepo.findById(brukerId).orElse(null);

        String feilmelding = Util.sjekkGyldigSpillOgBruker(spill, spillId, bruker, brukerId);
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

        return "redirect:/spill/{spillId}/spillsomdeltaker/{brukerId}";
    }

    @GetMapping("/feil")
    public String feil() {
        return "feil";
    }
}
