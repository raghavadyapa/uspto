package com.ipcybris.patent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class sgml {

    public static void main(String []args) throws ParserConfigurationException, IOException, SAXException {
        int count = 0;
        String ref = "<PATDOC DTD=\"2.4\" STATUS=\"BUILD 20010518\">";
        File file = new File("C:\\Users\\techolution\\Downloads\\sgml.sgm");
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append(ref);
        xmlBuilder.append("\n");
        String st;
        while ((st = br.readLine()) != null) {

            if (st.equalsIgnoreCase(ref)) {
                if (count > 0)
                    saveToMongo(xmlBuilder.toString());
                xmlBuilder = new StringBuilder();
//	                xmlBuilder.append("\n");
                count = count + 1;
            }
            xmlBuilder.append(st);
            xmlBuilder.append("\n");

        }
        saveToMongo(xmlBuilder.toString());
        //  System.out.println(count);
    }

    private static void saveToMongo(String toString) {

        System.out.println("in save to mongo method");
    }
}

