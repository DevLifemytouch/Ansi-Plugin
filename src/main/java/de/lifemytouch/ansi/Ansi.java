package de.lifemytouch.ansi;

import de.lifemytouch.ansi.challenge.ChallengeCommand;
import de.lifemytouch.ansi.challenge.ChallengeService;
import de.lifemytouch.ansi.challenge.item.ItemChallengeListener;
import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.listener.ChallengeInventoryListener;
import de.lifemytouch.ansi.challenge.mob.MobChallengeListener;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingManager;
import de.lifemytouch.ansi.fly.FlyCommand;
import de.lifemytouch.ansi.gamemode.GamemodeCommand;
import de.lifemytouch.ansi.player.listener.*;
import de.lifemytouch.ansi.rank.RankCommand;
import de.lifemytouch.ansi.report.ReportObservationService;
import de.lifemytouch.ansi.report.commands.ReportCommand;
import de.lifemytouch.ansi.report.ReportRepository;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.commands.ReportsCommand;
import de.lifemytouch.ansi.report.gui.ReportListHolder;
import de.lifemytouch.ansi.report.listener.ReportInventoryListener;
import de.lifemytouch.ansi.server.listener.MotdListener;
import de.lifemytouch.ansi.server.tab.TabListManager;
import de.lifemytouch.ansi.timer.TimerCommand;
import de.lifemytouch.ansi.challenge.tab.ChallengeTabCompleter;
import de.lifemytouch.ansi.rank.RankCompleter;
import de.lifemytouch.ansi.timer.TimerManager;
import de.lifemytouch.ansi.timer.TimerTabCompleter;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingGUI;
import de.lifemytouch.ansi.timer.TimerDisplay;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.vanish.VanishCommand;
import de.lifemytouch.ansi.vanish.VanishService;
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
    private ChallengeService challengeService;
    private ReportRepository reportRepository;
    private ReportService reportService;
    private VanishService vanishService;
    private ReportObservationService reportObservationService;

    static Color start = new Color(0, 105, 130);
    static Color end   = new Color(94, 234, 255);

    @Override
    public void onEnable() {

        // Core Systeme

        timerManager = new TimerManager(this);
        rankManager = new RankManager(this, TabListManager::updatePrefix);
        reportRepository = new ReportRepository(this);
        reportService = new ReportService(reportRepository);

        // Challenge Systeme

        itemChallengeManager = new ItemChallengeManager(this);
        mobChallengeManager = new MobChallengeManager(this);
        challengeSettingManager = new ChallengeSettingManager(this);
        challengeService = new ChallengeService(itemChallengeManager, mobChallengeManager, timerManager::reset);

        // Timer Tick

        getServer().getScheduler().runTaskTimer(
                this,
                timerManager::tick,
                20L,
                20L
        );

        // Challenge GUI

        ChallengeSettingGUI.init(challengeSettingManager);

        // Timer Systeme

        timerDisplay = new TimerDisplay(this, timerManager);
        timerDisplay.start();

        // Vanish Systeme

        vanishService = new VanishService(this);
        reportObservationService = new ReportObservationService(vanishService);

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

        // Commands
        getCommand("timer").setExecutor(new TimerCommand(timerManager));
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("rang").setExecutor(new RankCommand(rankManager));
        getCommand("challenge").setExecutor(new ChallengeCommand());
        getCommand("report").setExecutor(new ReportCommand(reportService));
        getCommand("reports").setExecutor(new ReportsCommand(reportService));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("vanish").setExecutor(new VanishCommand(vanishService));

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(rankManager, itemChallengeManager,
                mobChallengeManager, TabListManager::updatePrefix), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(rankManager, itemChallengeManager,
                mobChallengeManager, reportObservationService), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(timerManager::pause), this);
        getServer().getPluginManager().registerEvents(new ChallengeInventoryListener(challengeService,
                challengeSettingManager), this);
        getServer().getPluginManager().registerEvents(new ItemChallengeListener(itemChallengeManager), this);
        getServer().getPluginManager().registerEvents(new MotdListener(timerManager::getFormattedTime,
                itemChallengeManager::isActive, itemChallengeManager::getCollectedCount,
                itemChallengeManager::getTotalCount), this);
        getServer().getPluginManager().registerEvents(new MobChallengeListener(mobChallengeManager), this);
        getServer().getPluginManager().registerEvents(new BlockListener(timerManager::isRunning,
                challengeSettingManager::isBlockRandomizer), this);
        getServer().getPluginManager().registerEvents(new ReportInventoryListener(reportObservationService), this);

        // TabCompleter
        getCommand("rang").setTabCompleter(new RankCompleter());
        getCommand("timer").setTabCompleter(new TimerTabCompleter());
        getCommand("challenge").setTabCompleter(new ChallengeTabCompleter());
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }
}
