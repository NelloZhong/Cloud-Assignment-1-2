package twitter;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
 
public class OpenCSVParserExample {
 
    public static void main(String[] args) throws IOException {
 
        List<Tweets> emps = parseCSVFileLineByLine();
        System.out.println("**********");
        parseCSVFileAsList();
        System.out.println("**********");
        parseCSVToBeanList();
        System.out.println("**********");
        writeCSVData(emps);
    }
 
    private static void parseCSVToBeanList() throws IOException {
         
        HeaderColumnNameTranslateMappingStrategy<Tweets> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<Tweets>();
        beanStrategy.setType(Tweets.class);
         
        Map<String, String> columnMapping = new HashMap<String, String>();
        columnMapping.put("ID", "id");
        columnMapping.put("Name", "name");
        columnMapping.put("Role", "role");
        //columnMapping.put("Salary", "salary");
         
        beanStrategy.setColumnMapping(columnMapping);
         
        CsvToBean<Tweets> csvToBean = new CsvToBean<Tweets>();
        CSVReader reader = new CSVReader(new FileReader("Tweetss.csv"));
        List<Tweets> emps = csvToBean.parse(beanStrategy, reader);
        System.out.println(emps);
    }
 
    private static void writeCSVData(List<Tweets> emps) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer,'#');
        List<String[]> data  = toStringArray(emps);
        csvWriter.writeAll(data);
        csvWriter.close();
        System.out.println(writer);
    }
 
    private static List<String[]> toStringArray(List<Tweets> emps) {
        List<String[]> records = new ArrayList<String[]>();
        //add header record
        records.add(new String[]{"ID","Name","Role","Salary"});
        Iterator<Tweets> it = emps.iterator();
        while(it.hasNext()){
            Tweets emp = it.next();
            records.add(new String[]{emp.getDay(),emp.getHour(),emp.getUser(),emp.getText()});
        }
        return records;
    }
 
    private static List<Tweets> parseCSVFileLineByLine() throws IOException {
        //create CSVReader object
        CSVReader reader = new CSVReader(new FileReader("/Users/qianyizhong/Documents/Tweets.csv"), ',');
         
        List<Tweets> emps = new ArrayList<Tweets>();
        //read line by line
        String[] record = null;
        //skip header row
        reader.readNext();
         
        while((record = reader.readNext()) != null){
            Tweets emp = new Tweets();
            emp.setDay(record[0]);
            emp.setHour(record[1]);
            emp.setUser(record[2]);
            emp.setText(record[3]);
            emps.add(emp);
        }
         
        reader.close();
         
        System.out.println(emps);
        return emps;
    }
     
    private static void parseCSVFileAsList() throws IOException {
        //create CSVReader object
        CSVReader reader = new CSVReader(new FileReader("Tweetss.csv"), ',');
 
        List<Tweets> emps = new ArrayList<Tweets>();
        //read all lines at once
        List<String[]> records = reader.readAll();
         
        Iterator<String[]> iterator = records.iterator();
        //skip header row
        iterator.next();
         
        while(iterator.hasNext()){
            String[] record = iterator.next();
            Tweets emp = new Tweets();
            emp.setDay(record[0]);
            emp.setHour(record[1]);
            emp.setUser(record[2]);
            emp.setText(record[3]);
            emps.add(emp);
        }
         
        reader.close();
         
        System.out.println(emps);
    }
 
}