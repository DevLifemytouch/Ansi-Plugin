package de.lifemytouch.ansi.rank;

import org.bukkit.permissions.Permissible;

public enum Rank {

    OWNER(
            "ansi.rank.owner",
            "1_owner",
            9,
            "§x§8§2§0§0§0§0§lO§x§9§1§0§6§0§6§lW§x§9§F§0§C§0§C§lN§x§A§E§1§1§1§1§lE§x§B§C§1§7§1§7§lR §8| §7"
    ),
    DEV(
            "ansi.rank.dev",
            "2_dev",
            8,
            "§x§0§0§6§9§8§2§lD§x§0§C§8§4§9§B§lE§x§1§7§9§F§B§3§lV §8| §7"
    ),
    ADMIN(
            "ansi.rank.admin",
            "3_admin",
            7,
            "§x§F§C§0§4§0§4§lA§x§F§D§1§3§1§3§lD§x§F§E§2§2§2§2§lM§x§F§E§3§0§3§0§lI§x§F§F§3§F§3§F§lN §8| §7"
    ),
    MOD(
            "ansi.rank.mod",
            "4_mod",
            6,
            "§x§1§D§8§1§0§1§lM§x§3§5§9§1§1§1§lO§x§4§D§A§0§2§1§lD §8| §7"
    ),
    MEDIA(
            "ansi.rank.media",
            "5_media",
            5,
            "§x§7§6§0§0§B§E§lM§x§7§F§0§E§C§3§lE§x§8§7§1§C§C§8§lD§x§9§0§2§9§C§D§lI§x§9§8§3§7§D§2§lA §8| §7"
    ),
    VIPP(
            "ansi.rank.vipp",
            "6_vipp",
            4,
            "§x§D§B§0§0§C§C§lV§x§E§7§1§0§D§8§lI§x§F§3§2§1§E§5§lP§x§F§F§3§1§F§1§l+ §8| §7"
    ),
    VIP(
            "ansi.rank.vip",
            "7_vip",
            3,
            "§x§D§B§0§0§C§C§lV§x§E§D§1§9§D§F§lI§x§F§F§3§1§F§1§lP §8| §7"
    ),
    PREM(
            "ansi.rank.prem",
            "8_prem",
            2,
            "§x§D§B§B§2§0§0§lP§x§E§1§B§8§0§8§lR§x§E§7§B§F§0§F§lE§x§E§D§C§5§1§7§lM§" +
                    "x§F§3§C§B§1§E§lI§x§F§9§D§2§2§6§lU§x§F§F§D§8§2§D§lM §8| §7"
    ),
    DEFAULT(
            "ansi.rank.default",
            "9_default",
            0,
            "§7Player §8| §7"
    );

    private final String permission;
    private final String teamName;
    private final int weight;
    private final String prefix;

    Rank(String permission, String teamName, int weight, String prefix) {
        this.permission = permission;
        this.teamName = teamName;
        this.weight = weight;
        this.prefix = prefix;
    }

    public String getPermission() {
        return permission;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getWeight() {
        return weight;
    }

    public String getPrefix() {
        return prefix;
    }

    public static Rank getHighest(Permissible permissible) {
        Rank best = DEFAULT;
        for (Rank rank : values()) {
            if (permissible.hasPermission(rank.permission) && rank.weight > best.weight) {
                best = rank;
            }
        }
        return best;
    }

    public static Rank fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
