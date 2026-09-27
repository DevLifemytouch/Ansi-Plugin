package de.lifemytouch.ansi.rank;

import org.bukkit.permissions.Permissible;

import java.util.List;

public enum Rank {

    OWNER(
            "ansi.rank.owner",
            "1_owner",
            11,
            "§x§8§2§0§0§0§0§lO§x§9§1§0§6§0§6§lW§x§9§F§0§C§0§C§lN§x§A§E§1§1§1§1§lE§x§B§C§1§7§1§7§lR §8| §7",
            List.of("*")
    ),
    DEV(
            "ansi.rank.dev",
            "2_dev",
            10,
            "§x§0§0§6§9§8§2§lD§x§0§C§8§4§9§B§lE§x§1§7§9§F§B§3§lV §8| §7",
            List.of(
                    "ansi.punish.*",
                    "ansi.reports.*",
                    "ansi.commands.*",
                    "ansi.anticheat",
                    "ansi.anticheat.*",
                    "minecraft.command.*"
            )
    ),
    ADMIN(
            "ansi.rank.admin",
            "3_admin",
            9,
            "§x§F§C§0§4§0§4§lA§x§F§D§1§3§1§3§lD§x§F§E§2§2§2§2§lM§x§F§E§3§0§3§0§lI§x§F§F§3§F§3§F§lN §8| §7",
            List.of(
                    "ansi.punish.*",
                    "ansi.reports.*",
                    "ansi.commands.*",
                    "ansi.anticheat",
                    "ansi.anticheat.*"
            )
    ),
    MOD(
            "ansi.rank.mod",
            "4_mod",
            8,
            "§x§1§D§8§1§0§1§lM§x§3§5§9§1§1§1§lO§x§4§D§A§0§2§1§lD §8| §7",
            List.of(
                    "ansi.punish.ban",
                    "ansi.punish.kick",
                    "ansi.punish.mute",
                    "ansi.punish.history",
                    "ansi.punish.unpunish",
                    "ansi.commands.vanish",
                    "ansi.commands.fly",
                    "ansi.commands.gm",
                    "ansi.reports.handle",
                    "ansi.commands.lobby",
                    "ansi.commands.sc"
            )
    ),
    CONTENT(
            "ansi.rank.content",
            "5_content",
            8,
            "§x§F§F§3§F§3§F§lC§x§F§F§4§9§4§9§lO§x§F§F§5§3§5§3§lN§x§F§F§5§D§5§D§lT§x§F§F§6§6§6§6§lE" +
                    "§x§F§F§7§0§7§0§lN§x§F§F§7§A§7§A§lT §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby",
                    "ansi.commands.teleport",
                    "ansi.commands.coin.modify",
                    "ansi.commands.sc",
                    "ansi.reports.handle"
            )
    ),
    MEDIA(
            "ansi.rank.media",
            "6_media",
            5,
            "§x§7§6§0§0§B§E§lM§x§7§F§0§E§C§3§lE§x§8§7§1§C§C§8§lD§x§9§0§2§9§C§D§lI§x§9§8§3§7§D§2§lA §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    VIPP(
            "ansi.rank.vipp",
            "7_vipp",
            4,
            "§x§D§B§0§0§C§C§lV§x§E§7§1§0§D§8§lI§x§F§3§2§1§E§5§lP§x§F§F§3§1§F§1§l+ §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    VIP(
            "ansi.rank.vip",
            "8_vip",
            3,
            "§x§D§B§0§0§C§C§lV§x§E§D§1§9§D§F§lI§x§F§F§3§1§F§1§lP §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    PREM(
            "ansi.rank.prem",
            "9_prem",
            2,
            "§x§D§B§B§2§0§0§lP§x§E§1§B§8§0§8§lR§x§E§7§B§F§0§F§lE§x§E§D§C§5§1§7§lM§" +
                    "x§F§3§C§B§1§E§lI§x§F§9§D§2§2§6§lU§x§F§F§D§8§2§D§lM §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    DEFAULT(
            "ansi.rank.default",
            "a_default",
            1,
            "§7Player §8| §7",
            List.of(
                    "ansi.commands.lobby"
            )
    );

    private final String permission;
    private final String teamName;
    private final int weight;
    private final String prefix;
    private final List<String> permissions;

    Rank(String permission, String teamName, int weight, String prefix, List<String> permissions) {
        this.permission = permission;
        this.teamName = teamName;
        this.weight = weight;
        this.prefix = prefix;
        this.permissions = permissions;
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

    public List<String> getPermissions() {
        return permissions;
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

        if (name == null || name.isBlank()) {
            return null;
        }

        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
