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
                    "ansi.commands.lobby"
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
                    "ansi.commands.lobby"
            )
    ),
    TESTER(
            "ansi.rank.tester",
            "5_mod",
            8,
            "§x§1§E§D§2§F§D§lT§x§2§8§D§7§F§D§lE§x§3§2§D§B§F§E§lS§x§3§C§E§0§F§E§lT§x§4§6§E§4§F§F§lE" +
                    "§x§5§0§E§9§F§F§lR §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    BUILDER(
            "ansi.rank.builder",
            "6_mod",
            8,
            "§x§5§1§F§A§2§2§lB§x§5§B§F§B§2§A§lU§x§6§5§F§C§3§2§lI§x§7§0§F§D§3§A§lL§x§7§A§F§D§4§2§lD§" +
                    "x§8§4§F§E§4§A§lE§x§8§E§F§F§5§2§lR §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    MEDIA(
            "ansi.rank.media",
            "7_media",
            5,
            "§x§7§6§0§0§B§E§lM§x§7§F§0§E§C§3§lE§x§8§7§1§C§C§8§lD§x§9§0§2§9§C§D§lI§x§9§8§3§7§D§2§lA §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    VIPP(
            "ansi.rank.vipp",
            "8_vipp",
            4,
            "§x§D§B§0§0§C§C§lV§x§E§7§1§0§D§8§lI§x§F§3§2§1§E§5§lP§x§F§F§3§1§F§1§l+ §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    VIP(
            "ansi.rank.vip",
            "9_vip",
            3,
            "§x§D§B§0§0§C§C§lV§x§E§D§1§9§D§F§lI§x§F§F§3§1§F§1§lP §8| §7",
            List.of(
                    "ansi.commands.fly",
                    "ansi.commands.lobby"
            )
    ),
    PREM(
            "ansi.rank.prem",
            "a_prem",
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
            "b_default",
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
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
