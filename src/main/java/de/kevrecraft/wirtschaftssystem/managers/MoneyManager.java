package de.kevrecraft.wirtschaftssystem.managers;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLDataType;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLTabel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
