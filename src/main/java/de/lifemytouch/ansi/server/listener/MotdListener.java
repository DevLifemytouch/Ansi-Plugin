package de.lifemytouch.ansi.server.listener;

import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.timer.TimerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final TimerManager timerManager;
    private final ItemChallengeManager itemChallengeManager;

    public MotdListener(TimerManager timerManager, ItemChallengeManager itemChallengeManager) {
        this.timerManager = timerManager;
        this.itemChallengeManager = itemChallengeManager;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (itemChallengeManager.isActive()) {
            String CHALLENGE = "§x§B§0§9§5§2§1§lC§x§B§7§9§D§2§A§lh§x§B§E§A§5§3§3§la§x§C§5§A§D§3§C§ll§x§C§D§B§6" +
                    "§4§5§ll§x§D§4§B§E§4§E§le§x§D§B§C§6§5§7§ln§x§E§2§C§E§6§0§lg§x§E§9§D§6§6§9§le";
            String PROGRESS = "§x§A§8§2§1§B§0§lF§x§A§F§2§8§B§5§lo§x§B§5§2§F§B§A§lr§x§B§C§3§7§B§E§lt§x§C§2§3§E§" +
                    "C§3§ls§x§C§9§4§5§C§8§lc§x§C§F§4§C§C§D§lh§x§D§6§5§3§D§2§lr§x§D§C§5§B§D§6§li§x§E§3§6§2§D§B§" +
                    "lt§x§E§9§6§9§E§0§lt";
            event.setMotd(
                    CHALLENGE + " §7Zeit: §f" + timerManager.getFormattedTime()
                            + " §8|" + PROGRESS + "§7: "
                            + itemChallengeManager.getCollectedCount() + "/" + itemChallengeManager.getTotalCount()
            );
        } else {
            event.setMotd("§7Willkommen auf §6§lWaitinglobby's §7Testserver!");
        }
    }

}
