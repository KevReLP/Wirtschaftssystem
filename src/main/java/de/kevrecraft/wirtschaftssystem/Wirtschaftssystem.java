package de.kevrecraft.wirtschaftssystem;

import de.kevrecraft.wirtschaftssystem.managers.MoneyManager;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wirtschaftssystem extends JavaPlugin {

    MySQLConnection connection;
    MoneyManager moneyManager;


    @Override
    public void onEnable() {
        connection = new MySQLConnection("localhost", 3306, "minecraft", "minecraft", "minecraft");
        moneyManager = new MoneyManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MySQLConnection getConnection() {
        return connection;
    }

}
