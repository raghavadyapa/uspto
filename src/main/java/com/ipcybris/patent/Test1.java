package com.ipcybris.patent;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

////@Component
public class Test1 {
    public static void main(String[] args) //throws IOException, SAXException, ParserConfigurationException
     {
         Test1 t1=new Test1();
        String fp = "C:\\Users\\techolution\\Downloads\\grant_pdf_maint_M00718.tar";
        t1.test(fp);
    }

    //   @Autowired
    // private PRepo pRepo;

    private void test(String filePath) {
        //Open the file
        try {
            ZipFile file = new ZipFile(filePath);
            FileSystem fileSystem = FileSystems.getDefault();
            //Get file entries
            Enumeration<? extends ZipEntry> entries = file.entries();
            String uncompressedDirectory = "uncompressed/";
            if (!Files.exists(fileSystem.getPath(uncompressedDirectory))) {
                //We will unzip files in this folder
                Files.createDirectory(fileSystem.getPath(uncompressedDirectory));

            }
            //Iterate over entries
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                //If directory then create a new directory in uncompressed folder
                System.out.println(entry.getName());
                if (!entry.getName().contains("-SUPP")) {
                    if (entry.isDirectory()) {
                        System.out.println("Creating Directory:" + uncompressedDirectory + entry.getName());
                        Files.createDirectories(fileSystem.getPath(uncompressedDirectory + entry.getName()));
                    }
                    //Else create the file
                    else {
                        InputStream is = file.getInputStream(entry);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        int ind = entry.getName().indexOf("/");
                        String s = entry.getName().substring(0, ind);
                        String d = uncompressedDirectory + s;
                        File files = new File(d);
                        if (!files.exists()) {
                            if (files.mkdirs()) {
                                System.out.println("Multiple directories are created!");
                            } else {
                                System.out.println("Failed to create multiple directories!");
                            }
                        }
                        String uncompressedFileName = uncompressedDirectory + entry.getName();
                        FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                        while (bis.available() > 0) {
                            fileOutput.write(bis.read());
                        }
                        fileOutput.close();
                        bis.close();
                       /* if (uncompressedFileName.contains(".XML")) {
                            testXml(uncompressedFileName);

                        }*/

                        System.out.println("Written :" + entry.getName());
                    }

                    if (entry.getName().endsWith(".tar")) {
                        test("uncompressed/" + entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

   /* private  void testXml(String uncompressedFileName) throws ParserConfigurationException, SAXException, IOException {
      //  Patent patent = new Patent();
        File file2 = new File(uncompressedFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
        dbf.setFeature("http://xml.org/sax/features/validation", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        Document doc = dBuilder.parse(file2);
        NodeList rootNOde = doc.getElementsByTagName("us-bibliographic-data-grant");
        Element rootNOdeForAbstract = (Element)doc.getElementsByTagName("abstract").item(0);
        String abstraction = "No abstract available";
        if(rootNOdeForAbstract!=null){
            abstraction = rootNOdeForAbstract.getTextContent();
        }
        System.out.println("abstract :"+abstraction);

        patent.setAbstraction(abstraction);
        NodeList childNodes = rootNOde.item(0).getChildNodes();
        for(int i =0;i<childNodes.getLength();i++){
            Node childNode = childNodes.item(i);
            if(childNode.getNodeName().equals("publication-reference")){
                Element pReference = (Element) childNode;
                String patentNumber = pReference.getElementsByTagName("doc-number").item(0).getTextContent();
                patent.setPatentNumber(patentNumber);
                System.out.println("patent-number:"+patentNumber);
            }
            else if(childNode.getNodeName().equals("invention-title")){
                Element inventionTitle = (Element) childNode;
                String patentNumber = "NA";
                if(inventionTitle!=null) {
                    patentNumber = inventionTitle.getTextContent();
                    patent.setTitle(patentNumber);
                }
                System.out.println("title : "+patentNumber);
            }
            else if(childNode.getNodeName().equals("assignees")){
                List<String> assigneeList = new ArrayList<>();
                Element assignees = (Element) childNode;
                NodeList assigneesElementsByTagName = assignees.getElementsByTagName("assignee");
                if(assigneesElementsByTagName!=null){
                    for (int j=0;j<assigneesElementsByTagName.getLength();j++){
                        Node assigneeNode = assigneesElementsByTagName.item(j);
                        if(assigneeNode!=null){
                            Element assigneeElement = (Element)(assigneeNode);
                            NodeList assingeeElements = assigneeElement.getElementsByTagName("orgname");
                            if(assingeeElements!=null){
                                Node s =  assingeeElements.item(0);
                                String orgName = "NA";
                                if(s!=null){ orgName = s.getTextContent();}
                                if(orgName!=null){
                                    assigneeList.add(orgName);
                                }
                            }
                            else{
                                assigneeList.add("NA");
                            }
                        }
                    }}
                patent.setAssignee(assigneeList);
                System.out.println("assigneList:"+assigneeList);
            }
            else if(childNode.getNodeName().equals("us-parties")){
                List<String> assigneeList = new ArrayList<>();
                Element assignees = (Element) childNode;
                NodeList inventorsElementsByTagName = assignees.getElementsByTagName("inventors");
                Node assigneesElementsByTagName1 = inventorsElementsByTagName.item(0);
                Element element =  (Element) assigneesElementsByTagName1;
                NodeList assigneesElementsByTagName = element.getElementsByTagName("inventor");
                if(assigneesElementsByTagName!=null){
                    for (int j=0;j<assigneesElementsByTagName.getLength();j++){
                        Node assigneeNode = assigneesElementsByTagName.item(j);
                        if(assigneeNode!=null){
                            Element assigneeElement = (Element)(assigneeNode);
                            NodeList assingeeElements = assigneeElement.getElementsByTagName("first-name");
                            NodeList assingeeElementsLastName = assigneeElement.getElementsByTagName("last-name");
                            if(assingeeElements!=null){
                                String orgName = assingeeElements.item(0).getTextContent();
                                if(assingeeElementsLastName.item(0).getTextContent()!=null) {
                                    orgName = orgName +" " + assingeeElementsLastName.item(0).getTextContent();
                                }
                                if(orgName!=null){
                                    assigneeList.add(orgName);
                                }
                            }
                            else{
                                assigneeList.add("NA");
                            }
                        }
                    }}
                patent.setInventor(assigneeList);
                System.out.println("inventor list:"+assigneeList);

            }
            else if(childNode.getNodeName().equals("classification-cpc-text")){
                Element cpcText = (Element) childNode;
                String cpcClassification = "NA";
                if(cpcText!=null) {
                    cpcClassification = cpcText.getTextContent().substring(0,4);
                }
                patent.setClassification(cpcClassification);
                System.out.println("cpc classification: "+ cpcClassification);
            }
            else if(childNode.getNodeName().equals("classifications-cpc")){
                Element assignees = (Element) childNode;
                NodeList inventorsElementsByTagName = assignees.getElementsByTagName("main-cpc");
                Node assigneesElementsByTagName1 = inventorsElementsByTagName.item(0);
                Element element =  (Element) assigneesElementsByTagName1;
                NodeList assigneesElementsByTagName = element.getElementsByTagName("classification-cpc");
                String cpcClassification = "";
                if(assigneesElementsByTagName!=null) {
                    Node assigneeNode = assigneesElementsByTagName.item(0);
                    Element assigneeElement = (Element)(assigneeNode);
                    NodeList sectionList = assigneeElement.getElementsByTagName("section");
                    NodeList classList = assigneeElement.getElementsByTagName("class");
                    NodeList subclassList = assigneeElement.getElementsByTagName("subclass");
                    cpcClassification = sectionList.item(0).getTextContent()+classList.item(0).getTextContent()+subclassList.item(0).getTextContent();
                }
                patent.setClassification(cpcClassification);
                System.out.println("cpc classification: "+ cpcClassification);
            }
            else if(childNode.getNodeName().equals("us-references-cited")){
                Element assignees = (Element) childNode;
                NodeList inventorsElementsByTagName = assignees.getElementsByTagName("classification-cpc-text");
                Node assigneesElementsByTagName1 = inventorsElementsByTagName.item(0);
                Element element =  (Element) assigneesElementsByTagName1;
                String cpcClassification = "NA";
                if(element!=null) {
                    cpcClassification = element.getTextContent().substring(0,4);
                }
                patent.setClassification(cpcClassification);
                System.out.println("cpc classification: "+ cpcClassification);
            }


        }
        //pRepo.save(patent);


    }}*/
