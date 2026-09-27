package de.lifemytouch.ansi.anticheat.commands;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;
import de.lifemytouch.ansi.anticheat.violation.PlayerViolationData;
import de.lifemytouch.ansi.anticheat.violation.Violation;
import de.lifemytouch.ansi.anticheat.violation.ViolationManager;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AntiCheatCommand implements CommandExecutor {

    private final AntiCheatManager antiCheatManager;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.GERMAN);

    public AntiCheatCommand(AntiCheatManager antiCheatManager) {
        this.antiCheatManager = antiCheatManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if (!player.hasPermission("ansi.anticheat")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length == 0) {
            sendOverview(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "flags" -> handleFlags(player, args);
            case "history" -> handleHistory(player, args);
            case "check" -> handleCheck(player, args);
            case "debug" -> handleDebug(player, args);

            default -> sendHelp(player);
        }

        return false;
    }

    private void sendOverview(Player player) {
        player.sendMessage(" ");
        player.sendMessage("§c§lAntiCheat");
        player.sendMessage("§8----------------------");

        player.sendMessage("§7Checks: ");

        for(Check check : antiCheatManager.getChecks()) {
            String status = check.isEnabled() ? "§aAktiviert" : "§cDeaktiviert";

            player.sendMessage(" §8» §6" + check.getName() + " §8- " + status);
        }

        boolean alerts = antiCheatManager.getAlertService().hasAlertsEnabled(player);

        player.sendMessage(" ");

        player.sendMessage("§7Alerts: " + (alerts ? "§aAktiviert" : "§cDeaktiviert"));

        player.sendMessage(" ");
        player.sendMessage("§7Tipp: /ac help für alle Commands.");
    }

    private void handleFlags(Player player, String[] args) {
        if(!player.hasPermission("ansi.anticheat.alerts")) {
            player.sendMessage(Messages.getNO_PERMS());
            return;
        }

        if(args.length != 2 || !args[1].equalsIgnoreCase("toggle")) {
            player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7Nutze: /ac flags toggle");
            return;
        }

        antiCheatManager.getAlertService().toggle(player);
        boolean enabled = antiCheatManager.getAlertService().hasAlertsEnabled(player);

        player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7Alerts: " + (enabled ? "§aaktiviert" : "§cdeaktiviert"));
    }

    private void handleHistory(Player player, String[] args) {
        if(!player.hasPermission("ansi.anticheat.history")) {
            player.sendMessage(Messages.getNO_PERMS());
            return;
        }

        if(args.length != 2) {
            player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7Nutze: /ac history <Spieler>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);

        if(target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        PlayerViolationData data = antiCheatManager.getViolationManager().getData(target);

        if(data == null) {
            player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7Keine Flags für §c"
                    + target.getName() + "§7 gefunden");
            return;
        }

        sendHistory(player, target, data);
    }

    private void sendHistory(Player player, Player target, PlayerViolationData data) {
        player.sendMessage(" ");
        player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7History §8- §c" + target.getName());
        player.sendMessage("§8---------------------------");

        player.sendMessage("§7Total Flags: §c" + data.getTotalViolations());

        player.sendMessage(" ");
        player.sendMessage("§7Flags pro Check: ");

        data.getCheckViolations().forEach((check, violations) ->
                player.sendMessage(" §8» §c" + check + "§8- §c" + violations));

        player.sendMessage(" ");

        List<Violation> history = data.getHistory();
        player.sendMessage("§7Letzte Flags:");

        int start = Math.max(0, history.size() - 10);

        for(int i = history.size() - 1; i >= start; i--) {
            Violation violation = history.get(i);

            String time = timeFormat.format(new Date(violation.timestamp()));

            player.sendMessage(" §8[§7" + time + "§8] §c" + violation.check() + " §8(§e" + violation.amount()
                    + " VL§8) §7" + violation.information());
            player.sendMessage("§8---------------------------");
        }
    }

    private void handleCheck(Player player, String[] args) {
        if(!player.hasPermission("ansi.anticheat.manage")) {
            player.sendMessage(Messages.getNO_PERMS());
            return;
        }

        if(args.length != 3 || !args[2].equalsIgnoreCase("toggle")) {
            player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7Nutze: /ac check <Check> toggle");
            return;
        }

        Check check = antiCheatManager.getCheck(args[1]);

        if(check == null) {
            player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§cUnbekannter Check: §6" + args[1]);
            return;
        }

        check.setEnabled(!check.isEnabled());

        player.sendMessage(Messages.getANTICHEAT_PREFIX() + "§7" + check.getName() + " ist jetzt "
                + (check.isEnabled() ? "§aaktiviert" : "§cdeaktiviert") + "§7.");
    }

    private void sendHelp(Player player) {
        player.sendMessage(" ");
        player.sendMessage("§c§lAntiCheat");
        player.sendMessage("§8---------------------------");

        player.sendMessage("§7/ac §8- §7Übersicht");

        if(player.hasPermission("ansi.anticheat.alerts")) {
            player.sendMessage("§7/ac flags toggle §8- §7Alerts umschalten");
        }

        if(player.hasPermission("ansi.anticheat.history")) {
            player.sendMessage("§7/ac history <Spieler> §8- §7History eines Spielers anzeigen");
        }

        if(player.hasPermission("ansi.anticheat.manage")) {
            player.sendMessage("§7/ac check <Check> toggle §8- §7Check umschalten");
        }

        player.sendMessage("§8---------------------------");
    }

    private void handleDebug(
            Player player,
            String[] args
    ) {

        if (!player.hasPermission("ansi.anticheat.manage")) {
            player.sendMessage(Messages.getNO_PERMS());
            return;
        }

        if (args.length != 2) {
            player.sendMessage(
                    "§7Nutze: §f/ac debug <Anzahl>"
            );
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(
                    "§cDie Anzahl muss eine Zahl sein."
            );
            return;
        }

        if (amount <= 0 || amount > 100) {
            player.sendMessage(
                    "§cDie Anzahl muss zwischen 1 und 100 liegen."
            );
            return;
        }

        for (int i = 0; i < amount; i++) {

            antiCheatManager
                    .getViolationManager()
                    .flag(
                            player,
                            "Debug",
                            1.0,
                            "Test violation #" + (i + 1)
                    );
        }

        player.sendMessage(
                "§bAntiCheat §8» §7"
                        + amount
                        + " Test-Flags erzeugt."
        );
    }
}
