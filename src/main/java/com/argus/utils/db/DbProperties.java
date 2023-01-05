package com.argus.utils.db;

import lombok.Data;

/**
 * @author argus
 */
@Data
public class DbProperties {
    private String type;
    private String password;
    private String username;
    private String url;
    private String alias;
}
