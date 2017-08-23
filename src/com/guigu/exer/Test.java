package com.guigu.exer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lmk on 17-8-23.
 */
public class Test {

    public static void main(String[] args) {

        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                //int bb=b.compareTo(a);
               // System.out.println(bb);
                return b.compareTo(a);
            }
        });

        System.out.println("==================");
        Collections.sort(names, (String a, String b) -> b.compareTo(a));
    }

}
