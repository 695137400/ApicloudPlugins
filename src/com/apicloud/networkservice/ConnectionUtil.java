//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.networkservice;

import java.io.*;
import java.net.*;

public class ConnectionUtil {
    private static boolean isTrue;

    public ConnectionUtil() {
    }

    public static String getConnectionMessage(String data, String cookie) {
        URL url = null;
        HttpURLConnection conn = null;
        String line = "";
        String sTotalString = "";

        try {
            url = new URL(data);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }

            conn.getErrorStream();
            conn.getResponseCode();
            InputStream input = conn.getInputStream();

            BufferedReader reader;
            for(reader = new BufferedReader(new InputStreamReader(input, "utf-8")); (line = reader.readLine()) != null; sTotalString = sTotalString + line) {
            }

            reader.close();
            return sTotalString;
        } catch (Exception var14) {
            var14.printStackTrace();
            return "{ status:\"0\", msg:\"无法连接到云服务\"}";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

    }

    public static boolean isTrue() {
        return isTrue;
    }

    public static void setTrue(boolean isTrue) {
        ConnectionUtil.isTrue = isTrue;
    }
}
