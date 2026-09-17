package de.lifemytouch.ansi.coin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class CoinService {

    private final CoinRepository repository;
    private Consumer<Player> scoreboardUpdater;

    public CoinService(CoinRepository repository) {
        this.repository = repository;
    }

    public void setScoreboardUpdater(Consumer<Player> scoreboardUpdater) {
        this.scoreboardUpdater = scoreboardUpdater;
    }

    public long getCoins(UUID uuid) {
        return repository.getCoins(uuid);
    }

    public void setCoins(UUID uuid, long amount) {

        if (amount < 0) {
            amount = 0;
        }

        repository.setCoins(uuid, amount);

        updateScoreboard(uuid);
    }

    public void addCoins(UUID uuid, long amount) {

        if (amount <= 0) {
            return;
        }

        long current = getCoins(uuid);
        long newAmount = current + amount;

        if (newAmount < current) {
            newAmount = Long.MAX_VALUE;
        }

        setCoins(uuid, newAmount);
    }

    public void removeCoins(UUID uuid, long amount) {

        if (amount <= 0) {
            return;
        }

        long current = getCoins(uuid);
        long newAmount = Math.max(0, current - amount);

        setCoins(uuid, newAmount);
    }

    public boolean hasCoins(UUID uuid, long amount) {

        if (amount < 0) {
            return false;
        }

        return getCoins(uuid) >= amount;
    }

    public boolean isHidden(UUID uuid) {
        return repository.isHidden(uuid);
    }

    public void setHidden(UUID uuid, boolean hidden) {
        repository.setHidden(uuid, hidden);
    }

    private void updateScoreboard(UUID uuid) {

        if (scoreboardUpdater == null) {
            return;
        }

        Player player = Bukkit.getPlayer(uuid);

        if (player != null && player.isOnline()) {
            scoreboardUpdater.accept(player);
        }
    }
}