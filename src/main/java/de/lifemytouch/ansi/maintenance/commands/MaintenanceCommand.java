package de.lifemytouch.ansi.maintenance.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.maintenance.MaintenanceManager;
import de.lifemytouch.ansi.rank.Rank;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.UUID;

public class MaintenanceCommand implements CommandExecutor {

    private final MaintenanceManager maintenanceManager;
    private final RankManager rankManager;

    public MaintenanceCommand(
            MaintenanceManager maintenanceManager,
            RankManager rankManager
    ) {
        this.maintenanceManager = maintenanceManager;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        
        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.commands.maintenance")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if (args.length == 0) {

            if (maintenanceManager.isEnabled()) {
                player.sendMessage(Messages.getPREFIX() + "§7Status: §6Maintenance §aaktiviert§7.");
            } else {
                player.sendMessage(Messages.getPREFIX() + "§7Status: §6Maintenance §cdeaktiviert§7.");
            }

            return true;
        }

        switch (args[0].toLowerCase()) {

            case "add" -> addPlayer(player, args);

            case "remove" -> removePlayer(player, args);

            case "list" -> listPlayers(player);

            case "toggle" -> toggleMaintenance(player, args);

            default -> sendUsage(player);
        }

        return true;
    }

    private void addPlayer(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /maintenance §aadd §7<Spieler>");
            return;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        if (maintenanceManager.addPlayer(target)) {
            player.sendMessage(Messages.getPREFIX() + "§6" + args[1] + " §7kann jetzt auf den Server.");
        } else {
            player.sendMessage(Messages.getPREFIX() + "§6" + args[1] + " §7kann bereits auf den Server.");
        }
    }

    private void removePlayer(
            CommandSender player,
            String[] args
    ) {

        if (args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /maintenance §cremove §7<Spieler>");
            return;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        if (maintenanceManager.removePlayer(target)) {
            player.sendMessage(Messages.getPREFIX() + "§6" + args[1] + " §7kann jetzt nicht mehr auf den Server.");
        } else {
            player.sendMessage(Messages.getPREFIX() + "§6" + args[1] + " §7hat keinen Zugriff auf den Server.");
        }
    }

    private void listPlayers(Player player) {

        player.sendMessage(Messages.getPREFIX() + "§6§lMaintenance");

        player.sendMessage(
                "§7Status: "
                        + (maintenanceManager.isEnabled()
                        ? "§aaktiv"
                        : "§cdeaktiviert")
        );

        player.sendMessage("");

        if (maintenanceManager.getAllowedPlayers().isEmpty()) {

            player.sendMessage(
                    "§7Es können keine Spieler auf den Server."
            );

        } else {

            for (UUID uuid : maintenanceManager.getAllowedPlayers()) {

                OfflinePlayer offlinePlayer =
                        Bukkit.getOfflinePlayer(uuid);

                String name = player.getName();

                player.sendMessage("§8• §f" + name);
            }
        }

    }

    private void sendUsage(Player player) {

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/maintenance §7toggle"
        );

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/maintenance §aadd §7<Spieler>"
        );

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/maintenance §cremove §7<Spieler>"
        );
    }

    private boolean hasPermission(Player player) {

        Rank rank = rankManager.getRank(player);

        return rank == Rank.OWNER
                || rank == Rank.DEV
                || rank == Rank.ADMIN;
    }

    private void toggleMaintenance(Player player, String[] args) {
        maintenanceManager.toggle();

        if (maintenanceManager.isEnabled()) {
            player.sendMessage(Messages.getPREFIX() + "§7Die Maintenance wurde §aaktiviert§7.");
        } else {
            player.sendMessage(Messages.getPREFIX() + "§7Die Maintenance wurde  §cdeaktiviert§7.");
        }
    }
}