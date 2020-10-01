package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.inventory.PlayerInventorySlot;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum Items {

    IRON_SWORD(Material.IRON_SWORD, "demo.items.iron_sword", false, PlayerInventorySlot.HOTBAR_SLOT_1),
    BOW(Material.BOW, "demo.items.bow", true, PlayerInventorySlot.HOTBAR_SLOT_2),
    ARROW(Material.ARROW, "demo.items.arrow", false, PlayerInventorySlot.ROW_2_SLOT_5);


    private final Material material;
    private final String translation;
    private final boolean enchant;
    private final int slot;

    Items(Material material, String translation, boolean enchant, int slot) {
        this.material = material;
        this.translation = translation;
        this.enchant = enchant;
        this.slot = slot;
    }


}
