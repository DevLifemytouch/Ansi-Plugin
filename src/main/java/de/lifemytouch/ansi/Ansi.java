package de.lifemytouch.ansi;

import de.lifemytouch.ansi.build.BuildCommand;
import de.lifemytouch.ansi.build.BuildService;
import de.lifemytouch.ansi.coin.CoinRepository;
import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.coin.commands.CoinsCommand;
import de.lifemytouch.ansi.coin.completer.CoinsCompleter;
import de.lifemytouch.ansi.cosmetic.CosmeticRegistry;
import de.lifemytouch.ansi.cosmetic.CosmeticRepository;
import de.lifemytouch.ansi.cosmetic.CosmeticService;
import de.lifemytouch.ansi.fly.FlyCommand;
import de.lifemytouch.ansi.friend.FriendRepository;
import de.lifemytouch.ansi.friend.FriendService;
import de.lifemytouch.ansi.friend.commands.FriendCommand;
import de.lifemytouch.ansi.friend.completer.FriendTabCompleter;
import de.lifemytouch.ansi.gamemode.GamemodeCommand;
import de.lifemytouch.ansi.message.PrivateMessageRepository;
import de.lifemytouch.ansi.message.PrivateMessageService;
import de.lifemytouch.ansi.message.commands.MessageCommand;
import de.lifemytouch.ansi.player.listener.*;
import de.lifemytouch.ansi.playtime.PlaytimeRepository;
import de.lifemytouch.ansi.playtime.PlaytimeService;
import de.lifemytouch.ansi.playtime.commands.PlaytimeCommand;
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
import de.lifemytouch.ansi.server.scoreboard.ScoreboardListener;
import de.lifemytouch.ansi.server.scoreboard.ScoreboardManager;
import de.lifemytouch.ansi.server.tab.TabListManager;
import de.lifemytouch.ansi.rank.RankCompleter;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.staff.StaffChatListener;
import de.lifemytouch.ansi.staff.StaffChatService;
import de.lifemytouch.ansi.staff.StaffListCommand;
import de.lifemytouch.ansi.staff.commands.StaffChatCommand;
import de.lifemytouch.ansi.teleport.TeleportCommand;
import de.lifemytouch.ansi.vanish.VanishCommand;
import de.lifemytouch.ansi.vanish.VanishService;
import de.lifemytouch.ansi.world.commands.LobbyCommand;
import de.lifemytouch.ansi.world.commands.UUIDCommand;
import de.lifemytouch.ansi.world.listener.BlockListener;
import de.lifemytouch.ansi.world.listener.HotbarListener;
import de.lifemytouch.ansi.world.listener.InventoryListener;
import de.lifemytouch.ansi.world.listener.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    private FriendService friendService;
    private FriendRepository friendRepository;
    private CoinService coinService;
    private CoinRepository coinRepository;
    private CosmeticRepository cosmeticRepository;
    private CosmeticService cosmeticService;
    private CosmeticRegistry cosmeticRegistry;
    private ScoreboardManager scoreboardManager;
    private StaffChatService staffChatService;
    private PrivateMessageRepository privateMessageRepository;
    private PrivateMessageService privateMessageService;
    private PlaytimeRepository playtimeRepository;
    private PlaytimeService playtimeService;

    static Color start = new Color(0, 105, 130);
    static Color end   = new Color(94, 234, 255);

    @Override
    public void onEnable() {
        registerDependencies();
        registerCommands();
        registerListeners();
        registerCompleters();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(playtimeService::stop);
    }

    private void registerCommands() {
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
        getCommand("friend").setExecutor(new FriendCommand(friendService));
        getCommand("coins").setExecutor(new CoinsCommand(coinService));
        getCommand("uuid").setExecutor(new UUIDCommand());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand(staffChatService));
        getCommand("stafflist").setExecutor(new StaffListCommand(rankManager));
        getCommand("msg").setExecutor(new MessageCommand(privateMessageService));
        getCommand("playtime").setExecutor(new PlaytimeCommand(playtimeService));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(rankManager, scoreboardManager, playtimeService), this);
        getServer().getPluginManager().registerEvents(
                new PlayerQuitListener(this, rankManager, reportObservationService, reportService,
                        scoreboardManager, playtimeService), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new MotdListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(buildService), this);
        getServer().getPluginManager().registerEvents(new ReportInventoryListener(reportObservationService,
                punishmentService), this);
        getServer().getPluginManager().registerEvents(new PunishmentListener(punishmentService), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new HotbarListener(this, cosmeticService, cosmeticRegistry,
                coinService, friendService, rankManager, playtimeService), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(coinService, cosmeticService, cosmeticRegistry,
                this, friendService, rankManager, privateMessageService, playtimeService), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new StaffChatListener(this, staffChatService), this);
    }

    private void registerCompleters() {
        getCommand("rang").setTabCompleter(new RankCompleter());
        getCommand("ban").setTabCompleter(new BanTabCompleter());
        getCommand("friend").setTabCompleter(new FriendTabCompleter());
        getCommand("coins").setTabCompleter(new CoinsCompleter());
    }

    private void registerDependencies() {
        // Core Systeme

        rankManager = new RankManager(
                this,
                player -> {
                    scoreboardManager.updateScoreboard(player);
                    scoreboardManager.updateTabListForAll();
                }
        );
        reportRepository = new ReportRepository(this);
        reportService = new ReportService(reportRepository);

        coinRepository = new CoinRepository(this);
        coinService = new CoinService(coinRepository);

        cosmeticRepository = new CosmeticRepository(this);
        cosmeticService = new CosmeticService(cosmeticRepository);
        cosmeticRegistry = new CosmeticRegistry();

        staffChatService = new StaffChatService();


        friendRepository = new FriendRepository(this);
        friendService = new FriendService(friendRepository);

        privateMessageRepository = new PrivateMessageRepository(this);
        privateMessageService = new PrivateMessageService(
                privateMessageRepository,
                friendService
        );

        playtimeRepository = new PlaytimeRepository(this);
        playtimeService = new PlaytimeService(playtimeRepository);

        scoreboardManager = new ScoreboardManager(rankManager, coinService, playtimeService);

        coinService.setScoreboardUpdater(scoreboardManager::updateScoreboard);

        Bukkit.getScheduler().runTaskTimer(
                this,
                scoreboardManager::updateScoreboardForAll,
                20L * 60L,
                20L * 60L
        );

        Bukkit.getScheduler().runTaskTimer(
                this,
                () -> Bukkit.getOnlinePlayers().forEach(
                        playtimeService::saveOnlineSession
                ),
                20L * 60L * 5L,
                20L * 60L * 5L
        );

        Bukkit.getScheduler().runTaskTimer(
                this,
                rankManager::checkExpiredRanks,
                20L,
                20L * 60L
        );

        // Vanish Systeme

        vanishService = new VanishService(this);
        reportObservationService = new ReportObservationService(vanishService);

        // Punishment
        punishmentRepository = new PunishmentRepository(this);
        punishmentService = new PunishmentService(punishmentRepository);

        // Build
        buildService = new BuildService(this);

        // Friend
        friendService = new FriendService(friendRepository);
    }
}
