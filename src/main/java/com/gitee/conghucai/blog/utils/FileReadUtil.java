package com.gitee.conghucai.blog.utils;

import java.io.*;

public class FileReadUtil {
    private FileReadUtil() {
    }

    private static String markdownRead(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));

        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + System.lineSeparator();
        }

        return mdContent;
    }

    public static String readMDFile(String fileName){
        String content = "";
        File myMdFile = new File(fileName);
        try{
            content = markdownRead(myMdFile);
        } catch (IOException e) {
            content = "````文件未找到...请联系站点管理员````";
        }

        return content;
    }
}
