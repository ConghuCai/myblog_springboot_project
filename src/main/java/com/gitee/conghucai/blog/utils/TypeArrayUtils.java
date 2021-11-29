package com.gitee.conghucai.blog.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TypeArrayUtils {
    private TypeArrayUtils() {
    }

    public static List<String> getTypeList(List list){
        int n = list.size();
        List<String> typeList = new LinkedList<>();
        String[] type = {"info", "warning", "success", "danger"};

        Random rand = new Random();
        int lastIndex = -1;
        for(int i=0; i<n; i++){
            int index = rand.nextInt(type.length);
            if(index == lastIndex){
                index = (index + 1) % type.length;
            }
            String s = type[index];
            typeList.add(s);
            lastIndex = index;
        }

        return typeList;
    }
}
