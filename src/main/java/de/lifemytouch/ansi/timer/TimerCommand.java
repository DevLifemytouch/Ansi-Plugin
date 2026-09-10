package de.lifemytouch.ansi.timer;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor {

    private final TimerManager timerManager;

    public TimerCommand(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return false;

        if(!player.hasPermission("ansi.commands.timer")) {
            player.sendMessage(Messages.getNO_PERMS());
            return false;
        }

        if(args.length == 0) {
            player.sendMessage(Messages.getPREFIX() + "§7" + timerManager.getFormattedTime());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "resume":
                if(timerManager.isRunning()) {
                    player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7läuft bereits.");
                    return true;
                }
                timerManager.start();
                player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7wurde §agestartet§7.");
                break;

            case "pause":
                if(!timerManager.isRunning()) {
                    player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7läuft nicht.");
                    return false;
                }
                timerManager.pause();
                player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7wurde §cpausiert§7.");
                break;

            case "reset":
                if(!timerManager.isRunning()) {
                    player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7wurde §czurückgesetzt §7und §agestartet§7.");
                    timerManager.reset();
                    timerManager.start();
                }
                timerManager.reset();
                player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7wurde §czurückgesetzt§7.");
                break;

            case "stop":
                if (!timerManager.isRunning()) {
                    player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer läuft nicht");
                    return false;
                }
                timerManager.stop();
                player.sendMessage(Messages.getPREFIX() + "§7Der §6Timer §7wurde §cgestoppt§7.");
                break;
            default:
                player.sendMessage(Messages.getPREFIX() + "§7Dieser Command existiert nicht!");
        }

        return true;
    }
}
