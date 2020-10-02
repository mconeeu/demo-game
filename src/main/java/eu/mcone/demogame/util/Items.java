package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.inventory.PlayerInventorySlot;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum Items {

    /**
     * These are all the Items which will be set by the Minigame upon start of the {@link DemoIngameState}
     */
    IRON_SWORD(Material.IRON_SWORD, "demo.items.iron_sword", false, PlayerInventorySlot.HOTBAR_SLOT_1),
    BOW(Material.BOW, "demo.items.bow", true, PlayerInventorySlot.HOTBAR_SLOT_2),
    ARROW(Material.ARROW, "demo.items.arrow", false, PlayerInventorySlot.ROW_2_SLOT_5);


    /**
     * Contains the Material of the Item
     */
    private final Material material;
    /**
     * Contains the translation-key with which it will return the translated Name of the Item
     */
    private final String translation;
    /**
     * Return of the Item should be enchanted
     */
    private final boolean enchant;
    /**
     * Contains the Item {@link PlayerInventorySlot} in which the Item should be placed
     */
    private final int slot;

    /**
     * Constructs the Item
     *
     * @param material
     * @param translation
     * @param enchant
     * @param slot
     */
    Items(Material material, String translation, boolean enchant, int slot) {
        this.material = material;
        this.translation = translation;
        this.enchant = enchant;
        this.slot = slot;
    }


}
