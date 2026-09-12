package de.lifemytouch.ansi.gamemode;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (!player.hasPermission("ansi.commands.gm")) {
            player.sendMessage(Messages.getNO_PERMS());
            return false;
        }

        if(args.length == 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus: §6§l" + player.getGameMode());
        }

        String gamemode = args[0];
        System.out.println(gamemode);

        if (args.length == 1) {
            setPlayerGm(player, gamemode);
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (args.length == 2) {

            if(target == null) {
                player.sendMessage(Messages.getPREFIX() + "§7Spieler nicht online!");
                return false;
            }

            setTargetGm(player, target, gamemode);
            return true;
        }

        return false;
    }

    private void setPlayerGm(Player player, String gamemode) {
        switch (gamemode) {
            case "0":
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lSURVIVAL §7gesetzt.");
                break;
            case "1":
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lCREATIVE §7gesetzt.");
                break;
            case "2":
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lADVENTURE §7gesetzt.");
                break;
            case "3":
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lSPECTATOR §7gesetzt.");
                break;
        }
    }

    private void setTargetGm(Player player, Player target, String gamemode) {
        switch (gamemode) {
            case "0":
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lSURVIVAL §7gesetzt.");
                player.sendMessage(Messages.getPREFIX() + "§7Du hast den Spielmodus von §c" + target.getName() + "§7 auf " +
                "§6§lSURVIVAL §7gesetzt");
                break;
            case "1":
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lCREATIVE §7gesetzt.");
                player.sendMessage(Messages.getPREFIX() + "§7Du hast den Spielmodus von §c" + target.getName() + "§7 auf " +
                        "§6§lCREATIVE §7gesetzt");
                break;
            case "2":
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lADVENTURE §7gesetzt.");
                player.sendMessage(Messages.getPREFIX() + "§7Du hast den Spielmodus von §c" + target.getName() + "§7 auf " +
                        "§6§lADVENTURE §7gesetzt");
                break;
            case "3":
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(Messages.getPREFIX() + "§7Dein Spielmodus wurde auf §6§lSPECTATOR §7gesetzt.");
                player.sendMessage(Messages.getPREFIX() + "§7Du hast den Spielmodus von §c" + target.getName() + "§7 auf " +
                        "§6§lSPECTATOR §7gesetzt");
                break;
        }
    }
}
