package com.gitee.conghucai.blog.utils;

import java.io.*;

public class FileReadUtil {
    private FileReadUtil() {
    }

    public static String markdownRead(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));

        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }

        return mdContent;
    }
}
