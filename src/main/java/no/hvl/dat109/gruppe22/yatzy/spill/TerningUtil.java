package no.hvl.dat109.gruppe22.yatzy.spill;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TerningUtil {
    public static final int ANTALL_SIDER = 6;

    // Mapping fra terning-verdi til symbol
    public static final Map<Integer, String> TERNING_SYMBOLER = Map.of(
            1, "⚀",
            2, "⚁",
            3, "⚂",
            4, "⚃",
            5, "⚄",
            6, "⚅"
    );

    public static final Map<String, Integer> KUN_ENERE = Map.of(
            "t1", 1,
            "t2", 1,
            "t3", 1,
            "t4", 1,
            "t5", 1
    );

    public static int terningkast() {
        return ThreadLocalRandom.current().nextInt(ANTALL_SIDER) + 1;
    }

    public static Map<String, Integer> kastAlle() {
        return Map.of(
                "t1", terningkast(),
                "t2", terningkast(),
                "t3", terningkast(),
                "t4", terningkast(),
                "t5", terningkast()
        );
    }

    public static Map<String, String> terningVerdierTilSymboler(Map<String, Integer> terninger) {

        return terninger.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> TERNING_SYMBOLER.get(e.getValue())
                ));

    }

}
