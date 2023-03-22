package no.hvl.dat109.gruppe22.yatzy.spill;

import java.util.Map;

import static java.util.Map.entry;

public class Kombinasjonstyper {
    public static final int ANTALL_KOMBINASJONSTYPER = 15;

    public static final int AKTIV = -1;

    public static final int ENERE = 1;
    public static final int TOERE = 2;
    public static final int TREERE = 3;
    public static final int FIRERE = 4;
    public static final int FEMMERE = 5;
    public static final int SEKSERE = 6;
    public static final int ETT_PAR = 7;
    public static final int TO_PAR = 8;
    public static final int TRE_LIKE = 9;
    public static final int FIRE_LIKE = 10;
    public static final int LITEN_STRAIGHT = 11;
    public static final int STOR_STRAIGHT = 12;
    public static final int HUS = 13;
    public static final int SJANSE = 14;
    public static final int YATZY = 15;

    public static final Map<Integer, String> NAVN = Map.ofEntries(
            entry(ENERE, "enere"),
            entry(TOERE, "toere"),
            entry(TREERE, "treere"),
            entry(FIRERE, "firere"),
            entry(FEMMERE, "femmere"),
            entry(SEKSERE, "seksere"),
            entry(ETT_PAR, "ettpar"),
            entry(TO_PAR, "topar"),
            entry(TRE_LIKE, "trelike"),
            entry(FIRE_LIKE, "firelike"),
            entry(LITEN_STRAIGHT, "litenstraight"),
            entry(STOR_STRAIGHT, "storstraight"),
            entry(HUS, "hus"),
            entry(SJANSE, "sjanse"),
            entry(YATZY, "yatzy")
    );

    public static final Map<Integer, String> NAVN_UI = Map.ofEntries(
            entry(ENERE, "Enere"),
            entry(TOERE, "Toere"),
            entry(TREERE, "Treere"),
            entry(FIRERE, "Firere"),
            entry(FEMMERE, "Femmere"),
            entry(SEKSERE, "Seksere"),
            entry(ETT_PAR, "Ett par"),
            entry(TO_PAR, "To par"),
            entry(TRE_LIKE, "Tre like"),
            entry(FIRE_LIKE, "Fire like"),
            entry(LITEN_STRAIGHT, "Liten straight"),
            entry(STOR_STRAIGHT, "Stor straight"),
            entry(HUS, "Hus"),
            entry(SJANSE, "Sjanse"),
            entry(YATZY, "Yatzy")
    );

}
