package de.lifemytouch.ansi;

import de.lifemytouch.ansi.build.BuildCommand;
import de.lifemytouch.ansi.build.BuildService;
import de.lifemytouch.ansi.fly.FlyCommand;
import de.lifemytouch.ansi.gamemode.GamemodeCommand;
import de.lifemytouch.ansi.player.listener.*;
import de.lifemytouch.ansi.punish.PunishmentRepository;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.commands.*;
import de.lifemytouch.ansi.punish.completer.BanTabCompleter;
import de.lifemytouch.ansi.punish.listener.PunishmentListener;
import de.lifemytouch.ansi.rank.RankCommand;
import de.lifemytouch.ansi.report.ReportObservationService;
import de.lifemytouch.ansi.report.commands.ReportCommand;
import de.lifemytouch.ansi.report.ReportRepository;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.commands.ReportsCommand;
import de.lifemytouch.ansi.report.listener.ReportInventoryListener;
import de.lifemytouch.ansi.server.listener.MotdListener;
import de.lifemytouch.ansi.server.tab.TabListManager;
import de.lifemytouch.ansi.rank.RankCompleter;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.vanish.VanishCommand;
import de.lifemytouch.ansi.vanish.VanishService;
import de.lifemytouch.ansi.world.commands.LobbyCommand;
import de.lifemytouch.ansi.world.listener.BlockListener;
import de.lifemytouch.ansi.world.listener.HotbarListener;
import de.lifemytouch.ansi.world.listener.InventoryListener;
import de.lifemytouch.ansi.world.listener.WorldListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class Ansi extends JavaPlugin {

    private RankManager rankManager;
    private ReportRepository reportRepository;
    private ReportService reportService;
    private VanishService vanishService;
    private ReportObservationService reportObservationService;
    private PunishmentService punishmentService;
    private PunishmentRepository punishmentRepository;
    private BuildService buildService;

    static Color start = new Color(0, 105, 130);
    static Color end   = new Color(94, 234, 255);

    @Override
    public void onEnable() {

        // Core Systeme

        rankManager = new RankManager(this, TabListManager::updatePrefix);
        reportRepository = new ReportRepository(this);
        reportService = new ReportService(reportRepository);

        // Vanish Systeme

        vanishService = new VanishService(this);
        reportObservationService = new ReportObservationService(vanishService);

        // Punishment
        punishmentRepository = new PunishmentRepository(this);
        punishmentService = new PunishmentService(punishmentRepository);

        // Build
        buildService = new BuildService(this);

        register();
    }

    @Override
    public void onDisable() {
    }

    public void register() {

        // Commands
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("rang").setExecutor(new RankCommand(rankManager));
        getCommand("report").setExecutor(new ReportCommand(reportService));
        getCommand("reports").setExecutor(new ReportsCommand(reportService));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("vanish").setExecutor(new VanishCommand(vanishService));
        getCommand("ban").setExecutor(new BanCommand(punishmentService));
        getCommand("mute").setExecutor(new MuteCommand(punishmentService));
        getCommand("kick").setExecutor(new KickCommand(punishmentService));
        getCommand("history").setExecutor(new HistoryCommand(punishmentService));
        getCommand("unpunish").setExecutor(new UnpunishCommand(punishmentService));
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("build").setExecutor(new BuildCommand(buildService));

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(rankManager, TabListManager::updatePrefix), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(rankManager, reportObservationService), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new MotdListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(buildService), this);
        getServer().getPluginManager().registerEvents(new ReportInventoryListener(reportObservationService,
                punishmentService), this);
        getServer().getPluginManager().registerEvents(new PunishmentListener(punishmentService), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new HotbarListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        // TabCompleter
        getCommand("rang").setTabCompleter(new RankCompleter());
        getCommand("ban").setTabCompleter(new BanTabCompleter());
    }
}
