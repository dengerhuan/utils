package com.argus.utils.utils;


import com.argus.utils.ssh.SshClient;
import com.jcraft.jsch.JSchException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * @author argus_deng@163.com
 * @date 2022/12/12
 */
public class SshUtils {


    @NotNull
    public static SshClient dial(@NotNull String host, @Nullable String username, @Nullable String password) throws JSchException {
        SshClient client = new SshClient();
        client.dial(host, username, password);
        return client;
    }

    @NotNull
    public static SshClient dial(@NotNull String host, int port, @Nullable String username, @Nullable String password) throws JSchException {
        SshClient client = new SshClient();
        client.dial(host, port, username, password);
        return client;
    }

}
