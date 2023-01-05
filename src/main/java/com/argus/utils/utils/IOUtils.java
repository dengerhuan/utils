package com.argus.utils.utils;


import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author argus_deng@163.com
 * @date 2022/12/12
 */
public class IOUtils {

    /*
     read input stream to string and spline to \n
     */

    public static String toString(InputStream in, Charset charset) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charset));) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            return result.toString();
        }
    }

    public static String toString(InputStream in) throws IOException {
        return toString(in, StandardCharsets.UTF_8);
    }


    @NotNull
    public static ByteArrayOutputStream cloneInputStream(InputStream input) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos;
    }
}
