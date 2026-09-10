package de.lifemytouch.ansi.world;

import de.lifemytouch.ansi.challenge.setting.ChallengeSettingManager;
import de.lifemytouch.ansi.timer.TimerManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockListener implements Listener {

    private final TimerManager timerManager;
    private final ChallengeSettingManager challengeSettingManager;

    private final Map<Material, Material> blockMappings = new HashMap<>();
    private final List<Material> randomBlocks = new ArrayList<>();

    private final Random random = new Random();

    public BlockListener(TimerManager timerManager, ChallengeSettingManager challengeSettingManager) {
        this.timerManager = timerManager;
        this.challengeSettingManager = challengeSettingManager;

        loadRandomBlocks();
    }

    private void loadRandomBlocks() {

        for (Material material : Material.values()) {

            if (!material.isBlock()) {
                continue;
            }

            switch (material) {
                case AIR:
                case CAVE_AIR:
                case VOID_AIR:
                case BEDROCK:
                case BARRIER:
                case COMMAND_BLOCK:
                case CHAIN_COMMAND_BLOCK:
                case REPEATING_COMMAND_BLOCK:
                case STRUCTURE_BLOCK:
                case STRUCTURE_VOID:
                case JIGSAW:
                case LIGHT:
                case SPAWNER:
                    continue;
            }

            randomBlocks.add(material);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (!timerManager.isRunning()) {
            event.setCancelled(true);
            return;
        }

        if (!challengeSettingManager.isBlockRandomizer()) {
            return;
        }

        Block block = event.getBlock();
        Material original = block.getType();

        Material randomMaterial = blockMappings.get(original);

        if (randomMaterial == null) {
            randomMaterial = randomBlocks.get(
                    random.nextInt(randomBlocks.size())
            );

            blockMappings.put(original, randomMaterial);
        }

        event.setDropItems(false);

        block.getWorld().dropItemNaturally(
                block.getLocation(),
                new org.bukkit.inventory.ItemStack(randomMaterial)
        );
    }
}