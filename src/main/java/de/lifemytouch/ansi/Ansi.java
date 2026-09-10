package de.lifemytouch.ansi;

import de.lifemytouch.ansi.challenge.ChallengeCommand;
import de.lifemytouch.ansi.challenge.item.ItemChallengeListener;
import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.listener.ChallengeInventoryListener;
import de.lifemytouch.ansi.challenge.mob.MobChallengeListener;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingManager;
import de.lifemytouch.ansi.gamemode.GamemodeCommand;
import de.lifemytouch.ansi.player.listener.*;
import de.lifemytouch.ansi.rank.RankCommand;
import de.lifemytouch.ansi.server.listener.MotdListener;
import de.lifemytouch.ansi.timer.TimerCommand;
import de.lifemytouch.ansi.challenge.tab.ChallengeTabCompleter;
import de.lifemytouch.ansi.rank.RankCompleter;
import de.lifemytouch.ansi.timer.TimerManager;
import de.lifemytouch.ansi.timer.TimerTabCompleter;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingGUI;
import de.lifemytouch.ansi.timer.TimerDisplay;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.world.BlockListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class Ansi extends JavaPlugin {

    private TimerManager timerManager;
    private RankManager rankManager;
    private ItemChallengeManager itemChallengeManager;
    private MobChallengeManager mobChallengeManager;
    private TimerDisplay timerDisplay;
    private ChallengeSettingManager challengeSettingManager;

    static Color start = new Color(0, 105, 130);
    static Color end   = new Color(94, 234, 255);

    private static final String ansiGradient = "§x§0§0§6§9§8§2§lA§x§1§F§9§4§A§C§ln§x§3§F§B§F§D§5§ls§x§5§E§E§A§F§F§li";
    private static final String PREFIX = ansiGradient + " §8| §r";
    private static final String NO_PERMS = PREFIX + "§7Dazu hast du keine Rechte!";

    @Override
    public void onEnable() {
        timerManager = new TimerManager(this);
        itemChallengeManager = new ItemChallengeManager(this);
        mobChallengeManager = new MobChallengeManager(this);
        challengeSettingManager = new ChallengeSettingManager(this);

        getServer().getScheduler().runTaskTimer(
                this,
                timerManager::tick,
                20L,
                20L
        );

        ChallengeSettingGUI.init(challengeSettingManager);

        timerDisplay = new TimerDisplay(this, timerManager);
        timerDisplay.start();

        register();
    }

    @Override
    public void onDisable() {
        if (itemChallengeManager != null) itemChallengeManager.shutdown();
        if (mobChallengeManager != null) mobChallengeManager.shutdown();

        if (timerManager != null) {
            timerManager.pause();
            timerManager.save();
        }
    }

    public void register() {
        rankManager = new RankManager(this);

        // Commands
        getCommand("timer").setExecutor(new TimerCommand(timerManager));
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("rang").setExecutor(new RankCommand(rankManager));
        getCommand("challenge").setExecutor(new ChallengeCommand());

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(rankManager, itemChallengeManager,
                mobChallengeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(rankManager, itemChallengeManager,
                mobChallengeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new ChallengeInventoryListener(timerManager, itemChallengeManager,
                mobChallengeManager, challengeSettingManager), this);
        getServer().getPluginManager().registerEvents(new ItemChallengeListener(itemChallengeManager), this);
        getServer().getPluginManager().registerEvents(new MotdListener(timerManager, itemChallengeManager), this);
        getServer().getPluginManager().registerEvents(new MobChallengeListener(mobChallengeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(timerManager), this);
        getServer().getPluginManager().registerEvents(new BlockListener(timerManager, challengeSettingManager), this);

        // TabCompleter
        getCommand("rang").setTabCompleter(new RankCompleter());
        getCommand("timer").setTabCompleter(new TimerTabCompleter());
        getCommand("challenge").setTabCompleter(new ChallengeTabCompleter());
    }

    public static String getAnsiGradient() {
        return ansiGradient;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static String getNoPerms() {
        return NO_PERMS;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }
}
