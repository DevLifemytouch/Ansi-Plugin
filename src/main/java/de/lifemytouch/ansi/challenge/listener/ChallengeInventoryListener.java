package de.lifemytouch.ansi.challenge.listener;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.challenge.gui.ChallengeGUI;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingGUI;
import de.lifemytouch.ansi.challenge.gui.CustomizeChallengeGUI;
import de.lifemytouch.ansi.challenge.gui.CustomizeChallengeHolder;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingManager;
import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.timer.TimerManager;
import de.lifemytouch.ansi.challenge.ChallengeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChallengeInventoryListener implements Listener {

    private final TimerManager timerManager;
    private final ItemChallengeManager itemChallengeManager;
    private final MobChallengeManager mobChallengeManager;
    private final ChallengeSettingManager challengeSettingManager;

    public ChallengeInventoryListener(
            TimerManager timerManager,
            ItemChallengeManager itemChallengeManager,
            MobChallengeManager mobChallengeManager,
            ChallengeSettingManager challengeSettingManager
    ) {
        this.timerManager = timerManager;
        this.itemChallengeManager = itemChallengeManager;
        this.mobChallengeManager = mobChallengeManager;
        this.challengeSettingManager = challengeSettingManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        switch (event.getView().getTitle()) {
            case ChallengeGUI.TITLE:
                event.setCancelled(true);
                onClickedChallengeGUI(event, player);
                break;
            case CustomizeChallengeGUI.TITLE:
                onClickedCustomizeChallengeGUI(event, player);
                event.setCancelled(true);
                break;
            case ChallengeSettingGUI.TITLE:
                event.setCancelled(true);
                onClickedChallengeSettingGUI(event, player);
                break;
        }
    }

    private void onClickedChallengeGUI(InventoryClickEvent event, Player player) {
        if(event.getSlot() == 0) {
            CustomizeChallengeGUI.open(player, ChallengeType.ITEMS);
        }

        if(event.getSlot() == 1) {
            CustomizeChallengeGUI.open(player, ChallengeType.MOBS);
        }
    }

    private void onClickedCustomizeChallengeGUI(InventoryClickEvent event, Player player) {
        ChallengeType challengeType = getChallengeType(event);

        if(event.getSlot() == 0) {
            player.closeInventory();
            ChallengeGUI.open(player);
        } else if(event.getSlot() == 4) {
            timerManager.reset();

            if(challengeType == ChallengeType.MOBS) {
                mobChallengeManager.start(player);
                player.sendMessage(Ansi.getPREFIX() + "§7Die Challenge wurde gestartet!");
            } else if(challengeType == ChallengeType.ITEMS) {
                itemChallengeManager.start(player);
                player.sendMessage(Ansi.getPREFIX() + "§7Die Challenge wurde gestartet!");
            }

            player.closeInventory();
        } else if(event.getSlot() == 8) {
            player.sendMessage(Ansi.getPREFIX() + "§cWIP!");
        }
    }

    private void onClickedChallengeSettingGUI(InventoryClickEvent event, Player player) {

        if (event.getSlot() == 0) {
            challengeSettingManager.toggleHardcore(player.getWorld());
            player.sendMessage(
                    Ansi.getPREFIX() +
                            "§7Hardcore wurde " +
                            (challengeSettingManager.isHardcore()
                                    ? "§aaktiviert§7."
                                    : "§cdeaktiviert§7.")
            );
            ChallengeSettingGUI.open(player);
            return;
        }

        if (event.getSlot() == 1) {
            challengeSettingManager.toggleBlockRandomizer();
            player.sendMessage(
                    Ansi.getPREFIX() +
                            "§7Block Randomizer wurde " +
                            (challengeSettingManager.isBlockRandomizer()
                                    ? "§aaktiviert§7."
                                    : "§cdeaktiviert§7.")
            );
            ChallengeSettingGUI.open(player);
            return;
        }
    }

    private ChallengeType getChallengeType(InventoryClickEvent event) {
        Bukkit.getLogger().info("[DEBUG] Holder-Klasse: " + event.getInventory().getHolder());

        if (event.getInventory().getHolder() instanceof CustomizeChallengeHolder holder) {
            Bukkit.getLogger().info("[DEBUG] Typ aus Holder: " + holder.getChallengeType());
            return holder.getChallengeType();
        }
        Bukkit.getLogger().info("[DEBUG] Kein Holder gefunden -> Fallback ITEMS");
        return ChallengeType.ITEMS;
    }

}