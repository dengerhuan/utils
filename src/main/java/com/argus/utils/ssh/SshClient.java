package com.argus.utils.ssh;


import com.argus.utils.utils.IOUtils;
import com.jcraft.jsch.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author argus_deng@163.com
 * @date 2022/12/5
 */

public class SshClient implements Closeable {


    private String host;
    private JSch jSch;
    private int port;
    @Setter
    private int timeout;
    private String username;
    @Setter
    private String password;
    @Getter
    @Setter
    private Session session;

    public void dial(String host, String username, String password) throws JSchException {
        Assert.notNull(host, "Host is empty");

        this.dial(host, 22, username, password);
    }


    public void dial(String host, int port, String username, String password) throws JSchException {

        jSch = new JSch();
        this.host = host;
        this.password = password;
        this.username = username;
        this.timeout = 3000;
        this.port = port;
        newSession();
        session.connect();
    }


    private Session newSession() throws JSchException {
        Session session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(timeout);
        session.setPassword(password);
        this.setSession(session);
        return session;
    }

    /*
      安全的 open如果正常就回返回，异常抛丑并且close 资源
     */
    public ChannelSftp openSftp() throws JSchException {
        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect();
        return sftp;
    }


    public void put(String src, String dst, SftpProgressMonitor monitor, int mode) throws JSchException, SftpException {

        ChannelSftp sftp = openSftp();

        try {
            sftp.put(src, dst, monitor, mode);
        } finally {
            sftp.quit();
        }
    }


    public void get(String src, String dst, SftpProgressMonitor monitor, int mode) throws JSchException, SftpException {

        ChannelSftp sftp = openSftp();

        try {
            sftp.get(src, dst, monitor, mode);
        } finally {
            sftp.quit();
        }
    }

    public void get(String src, String dst) throws JSchException, SftpException {
        get(src, dst, null, Mode.OVERWRITE.ordinal());
    }

    public void put(String src, String dst) throws JSchException, SftpException {
        put(src, dst, null, Mode.OVERWRITE.ordinal());
    }

    /*
    所有的 动作包装类，都需要手动关闭通道， channdle open 以及connnect 异常不不要关闭
     有异常抛出原始异常
     */

    public List<ChannelSftp.LsEntry> ls(String path) throws JSchException, SftpException {

        ChannelSftp sftp = openSftp();

        final List<ChannelSftp.LsEntry> list = Collections.synchronizedList(new ArrayList<>());
        try {
            sftp.ls(path, (l) -> {
                list.add(l);
                return ChannelSftp.LsEntrySelector.CONTINUE;
            });
            return list;

        } finally {
            sftp.quit();
        }
    }


    public ChannelExec openExec(String cmd) throws JSchException {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(cmd);
        channel.connect();
        return channel;
    }

    public String combinedOutput(Session session, String cmd) throws JSchException {

        ChannelExec channel = openExec("exec");

        try (InputStream in = channel.getInputStream(); InputStream err = channel.getErrStream();) {
            String result = IOUtils.toString(in);
            String errInfo = IOUtils.toString(err);

            if (StringUtils.hasText(errInfo)) {
                throw new JSchException(result + errInfo);
            }
            return result;
        } catch (IOException e) {
            throw new JSchException(e.getMessage());
        } finally {
            channel.disconnect();
        }
    }

    @NotNull
    public String combinedOutputErrorAndInfo(@NotNull Session session, @NotNull String cmd) throws JSchException {

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(cmd);
        channel.connect();

        try (InputStream in = channel.getInputStream(); InputStream err = channel.getErrStream();) {
            String result = IOUtils.toString(in);
            String errInfo = IOUtils.toString(err);

            if (StringUtils.hasText(errInfo)) {
                return errInfo;
            }
            return result;
        } catch (IOException e) {
            throw new JSchException(e.getMessage());
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void close() {

        if (session != null) {

            this.session.disconnect();
        }
    }

    public enum Mode {
        /**
         * 完全覆盖模式，这是JSch的默认文件传输模式，即如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件。
         */
        OVERWRITE,
        /**
         * 恢复模式，如果文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件，则会从上一次中断的地方续传。
         */
        RESUME,
        /**
         * 追加模式，如果目标文件已存在，传输的文件将在目标文件后追加。
         */
        APPEND
    }
}
