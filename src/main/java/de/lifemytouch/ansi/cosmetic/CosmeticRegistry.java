package de.lifemytouch.ansi.cosmetic;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CosmeticRegistry {

    private final Map<String, Cosmetic> cosmetics = new LinkedHashMap<>();

    public CosmeticRegistry() {
        register(
                "astronaut",
                ItemBuilder.createCustomHead(
                        "223e1711ce6287cc9d8d83d76eec5d18efde2343e9ba1c41aa2acdbebcee0f41",
                        "§f§lAstronautenhelm",
                        List.of(
                                "§7Dieser Helm macht",
                                "§7dich zu einem Astronaut!"
                        )
                ),
                500
        );
    }

    private void register(String id, ItemStack item, long price) {
        cosmetics.put(
                id,
                new Cosmetic(id, item, price)
        );
    }

    public Cosmetic get(String id) {
        return cosmetics.get(id);
    }

    public Collection<Cosmetic> getAll() {
        return cosmetics.values();
    }
}