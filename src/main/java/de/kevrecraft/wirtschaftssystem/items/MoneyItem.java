package de.kevrecraft.wirtschaftssystem.items;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MoneyItem {

    private static NamespacedKey key;

    private static Wirtschaftssystem plugin;

    public MoneyItem(Wirtschaftssystem pl) {
        plugin = pl;
        key = new NamespacedKey(plugin, "Money");
    }

    public static ItemStack getItem(int value) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);


        item.setItemMeta(meta);
        return item;
    }

    public static NamespacedKey getKey() {
        return key;
    }


}
