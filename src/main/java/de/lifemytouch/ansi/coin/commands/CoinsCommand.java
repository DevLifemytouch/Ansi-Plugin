package de.lifemytouch.ansi.coin.commands;

import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    private final CoinService coinService;

    public CoinsCommand(CoinService coinService) {
        this.coinService = coinService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length == 0) {
            handleOwnBalance(player);
            return true;
        }

        if(args[0].equalsIgnoreCase("hide")) {
            handleHide(player, args);
            return true;
        }

        if(args[0].equalsIgnoreCase("set")
                || args[0].equalsIgnoreCase("add")
                || args[0].equalsIgnoreCase("remove")) {
            handleModify(player, args);
            return true;
        }

        handleOtherBalance(player, args);

        return true;
    }

    private void handleOwnBalance(Player player) {
        long coins = coinService.getCoins(player.getUniqueId());

        player.sendMessage(Messages.getPREFIX() + "§7Du hast §e" + coins + " Coins§7.");
    }

    private void handleOtherBalance(Player player, String[] args) {

        if(args.length != 1) {
            sendHelp(player);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        if(coinService.isHidden(target.getUniqueId())) {
            player.sendMessage(Messages.getPREFIX() + "§6" + target.getName() + "§7 hat seine Coins versteckt.");
            return;
        }

        long coins = coinService.getCoins(target.getUniqueId());

        player.sendMessage(Messages.getPREFIX() + "§6" + target.getName() + "§7 hat §e" + coins + " Coins§7.");

    }

    private void handleHide(Player player, String[] args) {

        if(args.length != 1) {
            player.sendMessage(Messages.getPREFIX() + "Nutze: /coins hide");
            return;
        }

        boolean hidden = coinService.isHidden(player.getUniqueId());

        coinService.setHidden(player.getUniqueId(), !hidden);

        if(hidden) {
            player.sendMessage(Messages.getPREFIX() + "§aDeine Coins sind jetzt wieder sichtbar.");
        } else {
            player.sendMessage(Messages.getPREFIX() + "§cDeine Coins sind jetzt versteckt.");
        }
    }

    private void handleModify(Player player, String[] args) {

        if(!player.hasPermission("ansi.commands.coins.modify")) {
            player.sendMessage(Messages.getNO_PERMS());
            return;
        }

        if(args.length != 3) {
            sendModifyHelp(player);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if(!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        long amount;

        try {
            amount = Long.parseLong(args[2]);
        } catch (NumberFormatException exception) {
            player.sendMessage(Messages.getPREFIX() + "§cDie Anzahl muss eine gültige Zahl sein!");
            return;
        }

        if(amount <= 0) {
            player.sendMessage(Messages.getPREFIX() + "§cDie Anzahl muss größer als 0 sein.");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "set" -> {
                coinService.setCoins(target.getUniqueId(), amount);

                player.sendMessage(Messages.getPREFIX() + "§7Die Coins von §6" +
                        target.getName() + "§7 wurden auf §e" + amount + " Coins §7gesetzt.");
            }

            case "add" -> {
                coinService.addCoins(target.getUniqueId(), amount);

                player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" +
                        target.getName() + "§e " + amount + " Coins §7gegeben.");
                player.sendMessage(Messages.getPREFIX() + "§6" +
                        target.getName() + "§7 hat nun §e" + coinService.getCoins(target.getUniqueId()) + " Coins§7.");
            }

            case "remove" -> {
                coinService.removeCoins(target.getUniqueId(), amount);

                player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" +
                        target.getName() + "§e " + amount + " Coins §7entfernt.");
                player.sendMessage(Messages.getPREFIX() + "§6" +
                        target.getName() + "§7 hat nun §e" + coinService.getCoins(target.getUniqueId()) + " Coins§7.");
            }
        }
    }

    private void sendModifyHelp(Player player) {
        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/coins §eset §7<Spieler> <Anzahl>"
        );

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/coins §aadd §7<Spieler> <Anzahl>"
        );

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7/coins §cremove §7<Spieler> <Anzahl>"
        );
    }

    private void sendHelp(Player player) {

    }
}
