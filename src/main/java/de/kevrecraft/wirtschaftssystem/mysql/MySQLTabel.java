package de.kevrecraft.wirtschaftssystem.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class MySQLTabel {

    private final MySQLConnection connection;
    private final String name;
    private final HashMap<String, MySQLDataType> colums;


    public MySQLTabel(MySQLConnection connection, String name, HashMap<String, MySQLDataType> colums) {
        this.connection = connection;
        this.name = name;
        this.colums = colums;
    }

    public void createTabel() {
        String sql = "CREATE TABEL " + name + "(";
        for (String colum : colums.keySet()) {
            sql += colum + " " + colums.get(colum).toSQL() + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ");";
        try {
            Statement statement = this.connection.getConnection().createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
