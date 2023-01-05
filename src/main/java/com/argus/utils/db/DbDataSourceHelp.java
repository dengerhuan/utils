package com.argus.utils.db;


import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author argus
 */
public class DbDataSourceHelp {


    public static DataSource createDriverManagerDataSource(DbProperties properties) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword());

        dataSource.setDriverClassName(DbDriverConstant.GetDriverClassName(properties.getType()));

        return dataSource;
    }


}
