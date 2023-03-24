package no.hvl.dat109.gruppe22.yatzy.spill;

import no.hvl.dat109.gruppe22.yatzy.model.Runderesultat;

import java.util.*;

import static no.hvl.dat109.gruppe22.yatzy.spill.Kombinasjonstyper.*;

public class Poengberegning {

    public static final int BONUS_GRENSE = 63;
    public static final int POENG_BONUS = 50;
    public static final int POENG_LITEN_STRAIGHT = 15;
    public static final int POENG_STOR_STRAIGHT = 20;
    public static final int POENG_YATZY = 50;

    private static int finnAntall(Collection<Integer> terninger, Integer verdi) {
        return (int) terninger.stream()
                .filter(i -> i.equals(verdi))
                .count();
    }

    public static int beregnEnere(Collection<Integer> terninger) {
        return finnAntall(terninger, 1);
    }

    public static int beregnToere(Collection<Integer> terninger) {
        return finnAntall(terninger, 2) * 2;
    }

    public static int beregnTreere(Collection<Integer> terninger) {
        return finnAntall(terninger, 3) * 3;
    }

    public static int beregnFirere(Collection<Integer> terninger) {
        return finnAntall(terninger, 4) * 4;
    }

    public static int beregnFemmere(Collection<Integer> terninger) {
        return finnAntall(terninger, 5) * 5;
    }

    public static int beregnSeksere(Collection<Integer> terninger) {
        return finnAntall(terninger, 6) * 6;
    }

    public static int beregnEttPar(Collection<Integer> terninger) {

        for (int verdi = 6; verdi >= 1; verdi--) {
            if (finnAntall(terninger, verdi) >= 2) {
                return verdi * 2;
            }
        }

        return 0;
    }

    public static int beregnToPar(Collection<Integer> terninger) {
        int poeng = 0;

        int verdi = 6;
        while (verdi >= 2 && poeng == 0) {
            if (finnAntall(terninger, verdi) >= 2) {
                poeng += verdi * 2;
            }
            verdi--;
        }

        if (poeng != 0) {
            while (verdi >= 1) {
                if (finnAntall(terninger, verdi) >= 2) {
                    return poeng + (verdi * 2);
                }
                verdi--;
            }
        }

        return 0;
    }

    public static int beregnTreLike(Collection<Integer> terninger) {

        for (int verdi = 6; verdi >= 1; verdi--) {
            if (finnAntall(terninger, verdi) >= 3) {
                return verdi * 3;
            }
        }

        return 0;
    }

    public static int beregnFireLike(Collection<Integer> terninger) {

        for (int verdi = 6; verdi >= 1; verdi--) {
            if (finnAntall(terninger, verdi) >= 4) {
                return verdi * 4;
            }
        }

        return 0;
    }

    public static int beregnLitenStraight(Collection<Integer> terninger) {
        Set<Integer> verdier = new HashSet<>(terninger);

        for (int verdi = 1; verdi <= 5; verdi++) {
            if (!verdier.contains(verdi)) {
                return 0;
            }
        }

        return POENG_LITEN_STRAIGHT;
    }

    public static int beregnStorStraight(Collection<Integer> terninger) {
        Set<Integer> verdier = new HashSet<>(terninger);

        for (int verdi = 2; verdi <= 6; verdi++) {
            if (!verdier.contains(verdi)) {
                return 0;
            }
        }

        return POENG_STOR_STRAIGHT;
    }

    public static int beregnHus(Collection<Integer> terninger) {
        int poeng = 0;

        int verdi1 = 6;
        while (verdi1 >= 1 && poeng == 0) {
            if (finnAntall(terninger, verdi1) >= 3) {
                poeng += verdi1 * 3;
            } else {
                verdi1--;
            }
        }

        if (poeng != 0) {
            int verdi2 = 6;
            while (verdi2 >= 1) {
                if (verdi2 != verdi1 && finnAntall(terninger, verdi2) >= 2) {
                    return poeng + (verdi2 * 2);
                }
                verdi2--;
            }
        }

        return 0;
    }

    public static int beregnSjanse(Collection<Integer> terninger) {
        return terninger.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static int beregnYatzy(Collection<Integer> terninger) {
        if (terninger.stream().distinct().count() == 1) {
            return POENG_YATZY;
        }

        return 0;
    }

    public static int beregnRunde(int kombinasjonstype, Collection<Integer> terninger) {
        int resultat = 0;

        switch (kombinasjonstype) {
            case ENERE:
                resultat = beregnEnere(terninger);
                break;
            case TOERE:
                resultat = beregnToere(terninger);
                break;
            case TREERE:
                resultat = beregnTreere(terninger);
                break;
            case FIRERE:
                resultat = beregnFirere(terninger);
                break;
            case FEMMERE:
                resultat = beregnFemmere(terninger);
                break;
            case SEKSERE:
                resultat = beregnSeksere(terninger);
                break;
            case ETT_PAR:
                resultat = beregnEttPar(terninger);
                break;
            case TO_PAR:
                resultat = beregnToPar(terninger);
                break;
            case TRE_LIKE:
                resultat = beregnTreLike(terninger);
                break;
            case FIRE_LIKE:
                resultat = beregnFireLike(terninger);
                break;
            case LITEN_STRAIGHT:
                resultat = beregnLitenStraight(terninger);
                break;
            case STOR_STRAIGHT:
                resultat = beregnStorStraight(terninger);
                break;
            case HUS:
                resultat = beregnHus(terninger);
                break;
            case SJANSE:
                resultat = beregnSjanse(terninger);
                break;
            case YATZY:
                resultat = beregnYatzy(terninger);
                break;
            default:
                throw new RuntimeException("Ugyldig kombinasjonstype");
        }

        return resultat;
    }

    public static int beregnRunde(Runderesultat rr) {
        return beregnRunde(rr.getKombinasjonstype(), rr.hentTerningverdier().values());
    }

    // Map som brukes av template for å vise poenglisten, keys må matche navnene som brukes i template-filen
    public static Map<String, Integer> genererPoengMap(
            Map<Integer, Runderesultat> resultater, Map<Integer, String> kombinasjonstyperNavn) {

        Map<String, Integer> poengMap = new HashMap<>(28);
        resultater.forEach((k, rr) -> poengMap.put(kombinasjonstyperNavn.get(k), beregnRunde(rr)));

        int totalUtenBonus = poengMap.values().stream().mapToInt(Integer::intValue).sum();

        if (resultater.containsKey(SEKSERE)) {
            int sumEnereTilSeksere = poengMap.get(kombinasjonstyperNavn.get(ENERE)) +
                    poengMap.get(kombinasjonstyperNavn.get(TOERE)) +
                    poengMap.get(kombinasjonstyperNavn.get(TREERE)) +
                    poengMap.get(kombinasjonstyperNavn.get(FIRERE)) +
                    poengMap.get(kombinasjonstyperNavn.get(FEMMERE)) +
                    poengMap.get(kombinasjonstyperNavn.get(SEKSERE));

            int bonus = (sumEnereTilSeksere >= BONUS_GRENSE) ? POENG_BONUS : 0;

            poengMap.put("sumEnereTilSeksere", sumEnereTilSeksere);
            poengMap.put("bonus", bonus);

            if (resultater.size() == ANTALL_KOMBINASJONSTYPER) {
                poengMap.put("totalt", totalUtenBonus + bonus);
            }
        }

        return poengMap;
    }

}
