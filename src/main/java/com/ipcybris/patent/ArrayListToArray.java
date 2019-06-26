package com.ipcybris.patent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArrayListToArray {

    public static void main(String []args) throws FileNotFoundException {
       File f=new File("C:\\Users\\techolution\\Downloads\\paths.txt");
       // BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\techolution\\Downloads\\paths.txt"));
        Scanner sr=new Scanner(f);
       // ArrayList<String> a=new ArrayList();
        String []a;
        List<String> res=new ArrayList<>();

        while(sr.hasNextLine())
        {
            String line=sr.nextLine();
            a=line.split(",");
            for (String path: a){
                res.add('"'+path+'"');
            }
        }


        System.out.println(res);


    }
}
