package de.lifemytouch.ansi.cosmetic;

import java.util.UUID;

public class CosmeticService {

    private final CosmeticRepository repository;

    public CosmeticService(CosmeticRepository repository) {
        this.repository = repository;
    }

    public boolean hasCosmetic(UUID uuid, String cosmeticId) {
        return repository.hasCosmetic(uuid, cosmeticId);
    }

    public void giveCosmetic(UUID uuid, String cosmeticId) {
        repository.setCosmetic(uuid, cosmeticId, true);
    }

    public void removeCosmetic(UUID uuid, String cosmeticId) {
        repository.setCosmetic(uuid, cosmeticId, false);
    }
}