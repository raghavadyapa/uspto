package com.ipcybris.patent;


import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlExtraction {


    public static void main(String[] args) throws Exception {
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

    /**
     * @param xmlData
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void saveToMongo(String xmlData) throws ParserConfigurationException, SAXException, IOException {
        //   System.out.println(xmlData);
        System.out.println("   ");
        System.out.println("dinesh test   ");
        System.out.println("   ");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File file = new File("C:\\Users\\techolution\\Downloads\\test.xml");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(xmlData);
        fileWriter.flush();
        fileWriter.close();
        Document doc = dBuilder.parse(file);
        System.out.print("************************************");
        System.out.println("Root element :" + doc.getDocumentElement());
        Element root = doc.getDocumentElement();

        NodeList child = root.getElementsByTagName("B100");
        int length = child.getLength();
        System.out.println(length);
       // System.out.println(child.getT);
        //Element bibli = (Element) child.item(1);
        //NodeList sub = bibli.getChildNodes();
        //NodeList nl = sub.get
        //System.out.println(len.getLength());
        /*int size=sub.getLength();
        for(int i=0;i<size;i++)
        {

            Node n=sub.item(i);
            if(n.getNodeType()==Node.ELEMENT_NODE)
            {
                Element ele=(Element)n;
                NodeList ssc=ele.getChildNodes();

            }*/

    }

    //System.out.println(bibli);

     /*   for(int i=0;i<length;i++)
        {
            Node n=child.item(i);
            if(n.getNodeType()==Node.ELEMENT_NODE)
            {
                Element ele=(Element)n;
                System.out.println(ele);
            }
        }*/
    // System.out.println(l);

    // NodeList nList2=doc.getElementsByTagName("abstract");
       /* for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
         //   Node nNode2=nList2.item(temp);
           // System.out.println("\nCurrent Element :" + nNode.getNodeName());
           // if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               // Element eElement = (Element) nNode;
                //System.out.println("abstract tag id is: " + eElement.getAttribute("id"));
                System.out.println("discription is : " + nNode.getTextContent());
              //  System.out.println("discription is : " + nNode2.getTextContent());
           // }
        }
        */

}