package de.kevrecraft.wirtschaftssystem.managers;

import de.kevrecraft.wirtschaftssystem.Wirtschaftssystem;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLDataType;
import de.kevrecraft.wirtschaftssystem.mysql.MySQLTabel;

import java.util.HashMap;

public class MoneyManager {

    private static MySQLTabel tabel;

    public MoneyManager(Wirtschaftssystem pl) {
        HashMap<String, MySQLDataType> colums = new HashMap<>();
        colums.put("uuid", MySQLDataType.CHAR);
        colums.put("value", MySQLDataType.INT);

        tabel = new MySQLTabel(pl.getConnection(), "money", colums);
    }


}
