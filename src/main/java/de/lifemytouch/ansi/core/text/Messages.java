package de.lifemytouch.ansi.core.text;

import de.lifemytouch.ansi.Ansi;

public class Messages {

    private Messages() {

    }

    private static final String ANSI_GRADIENT = "§x§0§0§6§9§8§2§lA§x§1§F§9§4§A§C§ln§x§3§F§B§F§D§5§ls§x§5§E§E§A§F§F§li";
    private static final String PREFIX = ANSI_GRADIENT + " §8| §r";
    private static final String NO_PERMS = PREFIX + "§7Dazu hast du keine Rechte!";

    public static String getPREFIX() {
        return PREFIX;
    }

    public static String getNO_PERMS() {
        return NO_PERMS;
    }

    public static String getANSI_GRADIENT() {
        return ANSI_GRADIENT;
    }
}
