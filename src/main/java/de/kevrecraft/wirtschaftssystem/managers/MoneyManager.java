package de.kevrecraft.wirtschaftssystem.managers;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import de.kevrecraft.wirtschaftssystem.items.MoneyItem;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLDataType;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLTabel;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class MoneyManager implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MySQLTabel.Condition condition = new MySQLTabel.Condition("uuid", event.getPlayer().getUniqueId().toString());
        if(!tabel.exits(condition)) {
            set(event.getPlayer().getUniqueId(), 500);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(event.getItem().getItemStack().getItemMeta().getPersistentDataContainer().has(MoneyItem.getKey(), PersistentDataType.INTEGER)) {
            int value = event.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(MoneyItem.getKey(), PersistentDataType.INTEGER);
            value *= event.getItem().getItemStack().getAmount();
            set(event.getPlayer().getUniqueId(), get(event.getPlayer().getUniqueId()) + value);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Du hast " + value + " aufgehoben!");
            event.setCancelled(true);
            event.getItem().remove();
            event.getPlayer().playSound(event.getItem().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2f, 1f);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity() instanceof Zombie) {
            event.getDrops().add(MoneyItem.getItem(50));
        }
    }


    private static MySQLTabel tabel;

    public MoneyManager(Wirtschaftssystem pl) {
        HashMap<String, MySQLDataType> colums = new HashMap<>();
        colums.put("uuid", MySQLDataType.CHAR);
        colums.put("value", MySQLDataType.INT);

        tabel = new MySQLTabel(pl.getConnection(), "money", colums);

        pl.getServer().getPluginManager().registerEvents(this, pl);

    }


    public static void set(UUID uuid, int value) {
        MySQLTabel.Condition condition = new MySQLTabel.Condition("uuid", uuid.toString());
        if(tabel.exits(condition)) {
            tabel.set("value", value, condition);
        } else {
            tabel.set("uuid", uuid.toString(), condition);
            tabel.set("value", value, condition);
        }
    }

    public static int get(UUID uuid) {
        MySQLTabel.Condition condition = new MySQLTabel.Condition("uuid", uuid.toString());
        if(tabel.exits(condition)) {
            return tabel.getInt("value", condition);
        }
        set(uuid, 0);
        return 0;
    }

}
