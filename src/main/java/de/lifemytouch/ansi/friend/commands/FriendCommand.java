package de.lifemytouch.ansi.friend.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.friend.FriendService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Member;
import java.nio.Buffer;
import java.util.Set;
import java.util.UUID;

public class FriendCommand implements CommandExecutor {

    private final FriendService friendService;

    public FriendCommand(FriendService friendService) {
        this.friendService = friendService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> handleAdd(player, args);
            case "remove" -> handleRemove(player, args);
            case "list" -> handleList(player);
            case "accept" -> handleAccept(player, args);
            case "deny" -> handleDeny(player, args);
            default -> sendHelp(player);
        }

        return true;
    }

    private void handleAdd(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /friend add <Spieler>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if(!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if(playerUUID.equals(targetUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§cDu kannst dich nicht selbst als Freund hinzufügen.");
            return;
        }

        if(friendService.areFriends(playerUUID, targetUUID)) {
            player.sendMessage("§cIhr seid bereits befreundet.");
            return;
        }

        if(friendService.hasPendingRequest(targetUUID, playerUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§cDu hast diesem Spieler bereits eine Anfrage gesendet.");
            return;
        }

        if(friendService.hasPendingRequest(playerUUID, targetUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§eDieser Spieler hat dir bereits eine " +
                    "Freundschaftsanfrage gesendet.");
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /friend §aaccept §6" +
                    player.getName() + "§7 um sie anzunehmen!");
            return;
        }

        friendService.sendRequest(playerUUID, targetUUID);

        player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" + target.getName()
                + "§7 eine Freundschaftsanfrage gesendet.");

        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if(onlineTarget != null) {
                onlineTarget.sendMessage(
                        Messages.getPREFIX() + "§aDu hast eine Freundschaftsanfrage von §6"
                                + player.getName() + "§a erhalten."
                );

                TextComponent accept = new TextComponent("§a§l[ANNEHMEN]");
                accept.setClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/friend accept " + player.getName()
                ));

                TextComponent deny = new TextComponent("§c§l[ABLEHNEN]");
                deny.setClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/friend deny " + player.getName()
                ));

                onlineTarget.spigot().sendMessage(
                        new ComponentBuilder()
                                .append(accept)
                                .append(" §7")
                                .append(deny)
                                .create()
                );
            }
        }
    }

    private void handleRemove(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /friend remove <player>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if (!friendService.areFriends(playerUUID, targetUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§cDu bist nicht mit §6" + target.getName() + "§c befreundet.");
            return;
        }

        friendService.removeFriend(playerUUID, targetUUID);

        player.sendMessage(Messages.getPREFIX() + "§aDu hast §6" + target.getName() +
                "§a aus deiner Freundesliste entfernt");

    }

    private void handleList(Player player) {

        Set<UUID> friends = friendService.getFriends(player.getUniqueId());

        if(friends.isEmpty()) {
            player.sendMessage(Messages.getPREFIX() + "§7Du hast momentan keine Freunde.");
            return;
        }

        player.sendMessage(Messages.getPREFIX() + "§6§lFreunde §8(" + friends.size() + ")");

        for(UUID uuid : friends) {
            OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

            String status;

            if(friend.isOnline()) {
                status = "§aOnline";
            } else {
                status = "§cOffline";
            }

            player.sendMessage("§7- §6" + friend.getName() + " §8» " + status);
        }

    }

    private void handleAccept(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /friend §aaccept §7<Spieler>");
            return;
        }

        OfflinePlayer sender = Bukkit.getOfflinePlayer(args[1]);

        UUID receiverUUID = player.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        if(!friendService.hasPendingRequest(receiverUUID, senderUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§cDieser Spieler hat dir keine Anfrage geschickt.");
            return;
        }

        friendService.acceptRequest(receiverUUID, senderUUID);

        player.sendMessage(Messages.getPREFIX() + "§aDu bist jetzt mit §6" + sender.getName() + "§a befreundet.");

        if(sender.isOnline()) {

            Player onlineSender = sender.getPlayer();

            if(onlineSender != null) {
                onlineSender.sendMessage(Messages.getPREFIX() + "§6" + player.getName() +
                        "§a hat deine Freundschaftsanfrage angenommen.");
            }

        }

    }

    private void handleDeny(Player player, String[] args) {

        if(args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /friend §cdeny §7<Spieler>");
            return;
        }

        OfflinePlayer sender = Bukkit.getOfflinePlayer(args[1]);

        UUID receiverUUID = player.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        if(!friendService.hasPendingRequest(receiverUUID, senderUUID)) {
            player.sendMessage(Messages.getPREFIX() + "§cDieser Spieler hat dir keine Anfrage geschickt.");
            return;
        }

        friendService.denyRequest(receiverUUID, senderUUID);

        player.sendMessage(Messages.getPREFIX() + "§cDu hast die Freundschaftsanfrage von §6" +
                sender.getName() + "§c abgelehnt");

        if(sender.isOnline()) {
            Player onlineSender = sender.getPlayer();

            if(onlineSender != null) {
                onlineSender.sendMessage(Messages.getPREFIX() + "§6" +
                        player.getName() + "§c hat deine Freundschaftsanfrage abgelehnt.");
            }
        }

    }
    private void sendHelp(Player player) {
        player.sendMessage(Messages.getPREFIX() + "§6§lFreunde");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §aadd §7<Spieler>");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §cremove §7<Spieler>");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §7list");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §aaccept §7<Spieler>");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §cdeny §7<Spieler>");
        player.sendMessage(Messages.getPREFIX()+ "§7/friend §7help");
    }
}
