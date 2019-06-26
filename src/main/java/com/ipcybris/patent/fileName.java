package com.ipcybris.patent;

import java.util.Arrays;

public class fileName {


    public String fName(String fp)
    {
        String [] words =fp.split("\\\\");
        //  System.out.println(Arrays.toString(words));
        // int l=words.length;
        // System.out.println(words[4]);
        String s1=words[words.length-1];       //.split(".");
        String[] s2=s1.split("\\.");
       // System.out.println(s2[0]);

        return s2[0];
    }

    public static void main(String []args)
    {
        fileName fn=new fileName();
        fn.fName("C:\\Users\\techolution\\Downloads\\20020101.zip");

    }
}
