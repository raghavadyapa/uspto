package com.techoluiton.cypris;


import com.mongodb.MongoClientURI;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadMongo {

  public static void main(String[] args) throws IOException, InvalidFormatException {
    long startTime = System.currentTimeMillis();
    MongoClientURI mongoClientURI =
            new MongoClientURI(
                    "");
    MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
    MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
    List<Patent> totalDocs = mongoTemplate.findAll(Patent.class,"missing-test");

    Workbook workbook = WorkbookFactory.create(new File("C:\\Users\\techolution\\Documents\\Final Categories.xlsx"));

    Sheet sheet=workbook.getSheetAt(0);
    int count=0;
    Map<String,TestCategory> map=new HashMap<String, TestCategory>();

    for(int i=0;i<sheet.getPhysicalNumberOfRows();i++) {
      Row r = sheet.getRow(i);
      String category = r.getCell(0).toString();
      String classification = r.getCell(2).toString();
      String code = r.getCell(1).toString();

      if(map.get(code)==null){

      map.put(code,new TestCategory(category,classification));
    }
    else{
        System.out.println("code: "+code);
      }
    }

    for (Patent p:totalDocs) {

      String classCode=p.getClassification();

      TestCategory tc=map.get(classCode);

      if(tc!=null){

        p.setClassificationText(tc.getClassification());
        p.setCategoryId(tc.getCategory());
        p.setCategory(tc.getCategory());
        count++;
        mongoTemplate.save(p,"missing-test");
        System.out.println(count);

      }

         }


//    mongoTemplate.(totalDocs,Patent.class);

    }

  }

