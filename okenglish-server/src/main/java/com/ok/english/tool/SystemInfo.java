package com.ok.english.tool;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class SystemInfo {
    public static String getSystemEnv() {
        Properties properties = System.getProperties();
        StringBuffer buffer = new StringBuffer();
        for (Object key : properties.keySet()) {
            String content = properties.getProperty(key.toString());
            if (content.trim().equals("")) continue;
            buffer.append(key);
            buffer.append(":  ");
            buffer.append(content);
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    public static String getFileContent(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.lines().forEach((item) -> {
                if (!item.trim().equals("")) {
                    buffer.append(item);
                    buffer.append("\r\n");
                }
            });
            reader.close();
        } catch (Exception e) {
            buffer.append("文件没有找到");
        }
        return buffer.toString();
    }
}