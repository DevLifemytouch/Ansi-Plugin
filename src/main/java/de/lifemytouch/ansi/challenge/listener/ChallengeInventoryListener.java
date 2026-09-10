package de.lifemytouch.ansi.challenge.listener;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.challenge.ChallengeService;
import de.lifemytouch.ansi.challenge.ChallengeType;
import de.lifemytouch.ansi.challenge.gui.ChallengeGUI;
import de.lifemytouch.ansi.challenge.gui.CustomizeChallengeGUI;
import de.lifemytouch.ansi.challenge.gui.CustomizeChallengeHolder;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingGUI;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChallengeInventoryListener implements Listener {

    private final ChallengeService challengeService;
    private final ChallengeSettingManager challengeSettingManager;

    public ChallengeInventoryListener(
            ChallengeService challengeService,
            ChallengeSettingManager challengeSettingManager
    ) {
        this.challengeService = challengeService;
        this.challengeSettingManager = challengeSettingManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        switch (event.getView().getTitle()) {

            case ChallengeGUI.TITLE -> {
                event.setCancelled(true);
                onClickedChallengeGUI(event, player);
            }

            case CustomizeChallengeGUI.TITLE -> {
                event.setCancelled(true);
                onClickedCustomizeChallengeGUI(event, player);
            }

            case ChallengeSettingGUI.TITLE -> {
                event.setCancelled(true);
                onClickedChallengeSettingGUI(event, player);
            }
        }
    }

    private void onClickedChallengeGUI(
            InventoryClickEvent event,
            Player player
    ) {
        if (event.getSlot() == 0) {
            CustomizeChallengeGUI.open(player, ChallengeType.ITEMS);
        }

        if (event.getSlot() == 1) {
            CustomizeChallengeGUI.open(player, ChallengeType.MOBS);
        }
    }

    private void onClickedCustomizeChallengeGUI(
            InventoryClickEvent event,
            Player player
    ) {
        ChallengeType challengeType = getChallengeType(event);

        if (event.getSlot() == 0) {

            player.closeInventory();
            ChallengeGUI.open(player);

        } else if (event.getSlot() == 4) {

            challengeService.start(player, challengeType);

            player.sendMessage(
                    Ansi.getPREFIX() +
                            "§7Die Challenge wurde gestartet!"
            );

            player.closeInventory();

        } else if (event.getSlot() == 8) {

            player.sendMessage(
                    Ansi.getPREFIX() + "§cWIP!"
            );
        }
    }

    private void onClickedChallengeSettingGUI(
            InventoryClickEvent event,
            Player player
    ) {
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
        }
    }

    private ChallengeType getChallengeType(
            InventoryClickEvent event
    ) {
        if (event.getInventory().getHolder()
                instanceof CustomizeChallengeHolder holder) {

            return holder.getChallengeType();
        }

        return ChallengeType.ITEMS;
    }
}