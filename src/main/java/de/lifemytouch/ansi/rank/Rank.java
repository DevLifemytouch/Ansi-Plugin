package de.lifemytouch.ansi.rank;

import org.bukkit.permissions.Permissible;

public enum Rank {

    OWNER(
            "ansi.rank.owner",
            "1_owner",
            3,
            "§x§8§2§0§0§0§0§lO§x§A§1§1§8§1§8§lW§x§C§1§2§F§2§F§lN§x§E§0§4§7§4§7§lE§x§F§F§5§E§5§E§lR §8| §7"
    ),
    DEV(
            "ansi.rank.dev",
            "2_dev",
            2,
            "§x§0§0§6§9§8§2§lD§x§2§F§A§A§C§1§lE§x§5§E§E§A§F§F§lV §8| §7"
    ),
    ADMIN(
            "ansi.rank.admin",
            "3_admin",
            1,
            "§x§F§F§2§5§2§5§lA§x§F§F§3§F§3§F§lD§x§F§F§5§9§5§9§lM§x§F§F§7§3§7§3§lI§x§F§F§8§D§8§D§lN §8| §7"
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
