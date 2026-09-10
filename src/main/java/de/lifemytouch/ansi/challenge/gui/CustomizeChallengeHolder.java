package de.lifemytouch.ansi.challenge.gui;

import de.lifemytouch.ansi.challenge.ChallengeType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomizeChallengeHolder implements InventoryHolder {

    private final ChallengeType challengeType;
    private Inventory inventory;

    public CustomizeChallengeHolder(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    public ChallengeType getChallengeType() {
        return challengeType;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

}
