package com.argus.utils.db;


import org.springframework.util.Assert;

import java.util.List;

/**
 * @author argus
 */
public class DbDriverConstant {

    public static final String MYSQL = "mysql";
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";


    public static final String SQLSERVER = "sqlserver";
    public static final String SQLSERVER_DRIVER = "com.microsoft.JDBC.sqlserver.SQLServerDriver";


    public static final String POSTGRESQL = "postgresql";
    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";


    public static final String ORACLE = "oracle";
    public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";


    public static final String SQLITE = "sqlite";
    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";


    public static final List<String> SUPPORT_LIST = List.of(MYSQL, ORACLE, SQLSERVER, POSTGRESQL, SQLITE);


    public static String GetDriverClassName(String dbType) {
        Assert.isTrue(SUPPORT_LIST.contains(dbType.toLowerCase()),"Unsupported database type");
        return switch (dbType.toLowerCase()) {
            case MYSQL -> MYSQL_DRIVER;
            case ORACLE -> ORACLE_DRIVER;
            case SQLSERVER -> SQLSERVER_DRIVER;
            case POSTGRESQL -> POSTGRESQL_DRIVER;
            case SQLITE -> SQLITE_DRIVER;
            default -> "";
        };
    }

}
