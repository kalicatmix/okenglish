package com.ok.english.tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdExecutor {
    public static String executeCmd(String cmd) {
        StringBuffer buffer = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            reader.lines().forEach((item) -> {
                if (!item.trim().equals("")) {
                    buffer.append(item);
                    buffer.append("\r\n");
                }
            });
            reader.close();
            process.destroy();
        } catch (Exception e) {
            buffer.append(e.toString());
        }
        return buffer.toString();
    }
}

