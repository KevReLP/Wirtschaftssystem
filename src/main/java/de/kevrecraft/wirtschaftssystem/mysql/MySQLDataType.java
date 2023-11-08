package de.kevrecraft.wirtschaftssystem.mysql;

public enum MySQLDataType {
    CHAR(255),
    BOOLEAN,
    INT(255);


    private final long size;

    MySQLDataType() {
        this.size = -1;
    }

    MySQLDataType( int size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String toSQL() {
        if(size >0)
            return this.name().toUpperCase() + "(" + this.size + ")";
        return this.name().toUpperCase();
    }

}
