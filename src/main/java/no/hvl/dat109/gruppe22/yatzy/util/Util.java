package no.hvl.dat109.gruppe22.yatzy.util;

import no.hvl.dat109.gruppe22.yatzy.model.Bruker;
import no.hvl.dat109.gruppe22.yatzy.model.Spill;

public class Util {

    /**
     * Sjekker om kombinasjonen av spill og bruker er gyldig (bruker er påmeldt det aktuelle
     * spillet, og spillet har startet)
     *
     * @param spill    det aktuelle spillet
     * @param spillId  spillet sin id
     * @param bruker   den aktuelle brukeren
     * @return         null hvis alt er OK, ellers en feilmelding
     */
    public static String sjekkGyldigSpillOgBruker(Spill spill, long spillId, Bruker bruker) {
        String feilmelding = null;

        if (spill == null) {
            feilmelding = String.format("Fant ikke spill med ID %d", spillId);
        } else if (!spill.getDeltakere().contains(bruker)) {
            feilmelding = String.format("Bruker med ID %d er ikke påmeldt spill med ID %d", bruker.getId(), spillId);
        } else if (!spill.getStartet()) {
            feilmelding = String.format("Spill med id %d har ikke startet ennå", spillId);
        }

        return feilmelding;
    }
}
