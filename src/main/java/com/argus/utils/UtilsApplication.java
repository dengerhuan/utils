package com.argus.utils;

import com.argus.utils.ssh.SshClient;
import com.argus.utils.utils.SshUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UtilsApplication {

    public static void main(String[] args) {


        try (SshClient client = SshUtils.dial("127.0.0.1", "Argus", "Nokia8g!");) {

            client.put("./pom.xml", "/tmp");

            client.ls("/tmp").forEach(l -> {


                String name = l.getFilename();


                if (l.getAttrs().isReg()) {
                    try {
                        System.out.println(l.getAttrs().isReg());
                        client.get("/tmp/" + name, "./temp/" + name);
                    } catch (JSchException e) {
                        System.out.println(e);
                        System.out.println(e.toString());
//                        throw new RuntimeException(e);
                    } catch (SftpException e) {
//                        throw new RuntimeException(e);

                        System.out.println(l);
                        System.out.println(l.getAttrs());
                        System.out.println(e.toString());
                    }

//
                }
            });
        } catch (JSchException | SftpException ee) {

            System.out.println(ee);
        }
//
////        Session session = session = client.newSession();
//        try {
//
////            session.connect();
//
//
////            client.put(session, "./pom.xml", "/data01/CounterData/nokia");
////
////            client.ls(session, "/tmp").forEach(l -> {
////                System.out.println(l);
////            });
//        } catch (JSchException | SftpException e) {
//
//            System.out.println(e);
//        } finally {
//            session.disconnect();
//        }
//
//        System.out.println(session.isConnected());
//        System.out.println(session.getClientVersion());


    }

}
