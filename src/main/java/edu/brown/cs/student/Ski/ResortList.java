package edu.brown.cs.student.Ski;

import edu.brown.cs.student.CSVCode.Parsing.CreatorFromRow;
import edu.brown.cs.student.CSVCode.Parsing.FactoryFailureException;
import edu.brown.cs.student.CSVCode.Parsing.Parse;

import edu.brown.cs.student.Ski.Records.ResortInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResortList {

    private String path;
    private CreatorFromRow <List<String>> resortCreator;
   // private List<List<String>> resortList;
    private HashMap<String, ResortInfo> resortMap;

    public ResortList() throws IOException {
        String filePath = new File("").getAbsolutePath();
        System.out.println(filePath);
        this.path = filePath + "/data/Sk Resort Prices, Names, Location.csv";
        this.creatorNameHelper();
        this.parseResorts();
        //System.out.println(this.resortMap);
    }

    private void creatorNameHelper(){
        this.resortCreator =
                new CreatorFromRow <List<String>>() {
                    @Override
                    public List<String> create(List<String> row) throws FactoryFailureException {
                        List<String> locationList = new ArrayList<String>();
                        locationList.add(row.get(0));
                        locationList.add(row.get(1));
                        locationList.add(row.get(2));
                        return locationList;
                    }
                };
    }

    private void parseResorts(){
        try{
            Reader reader = new FileReader(this.path);
            Parse<List<String>> resortParser = new Parse<List<String>>(reader, this.resortCreator);
            List<List<String>> resortList = resortParser.parse();
            //System.out.println(resortList);

            this.resortMap = new HashMap<String, ResortInfo>();
            for (int i = 1; i < resortList.size(); i++) {
                ResortInfo info = new ResortInfo(resortList.get(i).get(1), resortList.get(i).get(2), resortList.get(i).get(0));
                this.resortMap.put(resortList.get(i).get(1).toLowerCase(), info);
            }
        } catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        }
    }

    public HashMap<String, ResortInfo> getResortMap(){
        return this.resortMap;
    }

    public List<String> getResortNames(){
        ArrayList<String> names = new ArrayList<>();

        ArrayList<ResortInfo> info = new ArrayList<>(this.resortMap.values());
        for (int i = 0; i < info.size(); i++) {
            names.add(info.get(i).resortName());
        }
        return names;
    }

    public List<ResortInfo> getResortCountries(){
        return new ArrayList<>(this.resortMap.values());
    }
}
