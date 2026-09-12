package de.lifemytouch.ansi.punish.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.Punishment;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnpunishCommand implements CommandExecutor {

    private final PunishmentService punishmentService;

    public UnpunishCommand(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.punish.unpunish")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /unpunish <Spieler>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        Punishment punishment = punishmentService.getActivePunishment(target.getUniqueId(), PunishmentType.BAN);

        if(punishment == null) {
            punishment = punishmentService.getActivePunishment(target.getUniqueId(), PunishmentType.MUTE);
        }

        if(punishment == null) {
            player.sendMessage(Messages.getPREFIX() + "§7Der Spieler §6 " +
                    target.getName() + "§7 hat keine aktive Sperre.");
            return true;
        }

        boolean revoked = punishmentService.revokePunishment(target.getUniqueId(), punishment.getPunishmentType());

        if(!revoked) {
            player.sendMessage(Messages.getPREFIX() + "§7Die Sperre konnte nicht aufgehoben werden.");
            return true;
        }

        player.sendMessage(Messages.getPREFIX() + "§7Die Sperre von §6 "+ target.getName() + "§7 wurde aufgehoben.");

        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if(onlineTarget != null) {
                onlineTarget.sendMessage(Messages.getPREFIX() + "§7Deine Sperre wurde aufgehoben!");
                onlineTarget.playSound(onlineTarget, Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
            }
        }

        return true;
    }
}
