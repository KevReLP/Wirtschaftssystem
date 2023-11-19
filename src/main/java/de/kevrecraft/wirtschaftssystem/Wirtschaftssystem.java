package de.kevrecraft.wirtschaftssystem;

import de.kevrecraft.wirtschaftssystem.commands.MoneyCommand;
import de.kevrecraft.wirtschaftssystem.items.MoneyItem;
import de.kevrecraft.wirtschaftssystem.managers.BarralShopManager;
import de.kevrecraft.wirtschaftssystem.managers.MoneyManager;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wirtschaftssystem extends JavaPlugin {

    MySQLConnection connection;
    MoneyManager moneyManager;
    BarralShopManager barralShopManager;


    @Override
    public void onEnable() {
        connection = new MySQLConnection("localhost", 3306, "minecraft", "minecraft", "minecraft");
        moneyManager = new MoneyManager(this);
        barralShopManager = new BarralShopManager(this);

        getCommand("money").setExecutor(new MoneyCommand(this));

        new MoneyItem(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        barralShopManager.onDisable();
    }

    public MySQLConnection getConnection() {
        return connection;
    }

}
