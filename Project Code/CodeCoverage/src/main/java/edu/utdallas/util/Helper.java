package edu.utdallas.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Helper {
	
	public static HashMap<String, HashSet<String>> hashmap = new HashMap<String, HashSet<String>>();
	
	public static HashMap<String, HashSet<String>> tcr = new HashMap<String, HashSet<String>>();
	
	public static HashSet<String> tests = new HashSet<String>();
	
	public static HashSet<String> tests_class = new HashSet<String>();
	
	public static String testPath;
	
	public static int totalLines = 0;
	
	public static void addExecutedLine(String className) {
		if (hashmap.containsKey(Helper.testPath)) {
			hashmap.get(Helper.testPath).add(className);
		} 
		/*else {
			hashmap.put(Helper.testPath, new HashSet<String>());
			hashmap.get(Helper.testPath).add(className);
		}*/
		
	}
	
	public static void findTests(ArrayList<String> classes) {
		for (String c : classes) {
			/*
			System.out.println("class: " + c);
			if (Helper.tcr.get(c) != null) {
				Helper.tests.addAll(Helper.tcr.get(c));
			}
			*/
			Iterator iter = Helper.tcr.entrySet().iterator();
            while (iter.hasNext()) {
            	Map.Entry entry = (Map.Entry) iter.next();
            	String key = (String)entry.getKey();
            	HashSet<String> val = (HashSet<String>)entry.getValue();
            	if (Helper.isSameClass(c, key)) Helper.tests.addAll(Helper.tcr.get(key));
            }
        }
		
		for (String t : Helper.tests) {
			System.out.println(t + " need to be executed.");
		}
		
	}
	
	public static void totalLinesPlus() {
		Helper.totalLines++;
	}
	
	public static void writeTestClassRelationship() {
		File file = new File(System.getProperty("user.dir") + "/test-class.txt");
		if(!file.exists()){
			//System.out.println("no");
			Iterator iter = Helper.hashmap.entrySet().iterator();
            while (iter.hasNext()) {
            	Map.Entry entry = (Map.Entry) iter.next();
            	String key = (String)entry.getKey();
            	HashSet<String> val = (HashSet<String>)entry.getValue();
            	for (String info : val) {
            		//System.out.println(info);
            		String cname = info.split(":")[0];
            		if (!Helper.tcr.containsKey(cname)){
            			HashSet<String> tmp = new HashSet<String>();
            			Helper.tcr.put(cname, tmp);
            		}
            		Helper.tcr.get(cname).add(key);
            	}
            }
            
            //write into file
            PrintWriter writer = null;
    		try {
    			writer = new PrintWriter(file);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    		//writer.println(info);
    		Iterator tcr_iter = Helper.tcr.entrySet().iterator();
            while (tcr_iter.hasNext()) {
            	Map.Entry tcr_entry = (Map.Entry) tcr_iter.next();
            	String tcr_key = (String)tcr_entry.getKey();
            	HashSet<String> tcr_val = (HashSet<String>)tcr_entry.getValue();
            	writer.print(tcr_key);
            	for (String info : tcr_val) {
            		//System.out.println("aabbcc");
            		writer.print("|" + info);
            	}
            	writer.print("||");
            }
            writer.close();
		}
		//System.out.println("tcr size: " + Helper.tcr.size());
	}
	
	public static void readTestClassRelationship() {
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(System.getProperty("user.dir") + "/test-class.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line;
            
            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                String[] ct = line.split("\\|\\|");
                for (int c = 0; c < ct.length; c++) {
                	String[] one = ct[c].split("\\|");
                	for (int i = 0; i < one.length; i++) {
                		if (i == 0) {
                			Helper.tcr.put(one[0], new HashSet<String>());
                			//System.out.println("one[0]" + one[0]);
                		}
                		else {
                			Helper.tcr.get(one[0]).add(one[i]);
                			//System.out.println("one[i]" + one[i]);
                		}
                	}
                }
            }   

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");
        }
        catch(IOException ex) {
            System.out.println("Error reading file");
        }
	}
	
	public static boolean isSameClass(String apath, String ppath) {
		
		String[] apath_arr = apath.split("\\.java")[0].split("\\/");
		String[] ppath_arr = ppath.split("\\.");
		for (int i = 1; i <= 3; i++) {
			//System.out.println("a: " + apath_arr[apath_arr.length - i]);
			//System.out.println("p: " + ppath_arr[ppath_arr.length - i]);
			if (!apath_arr[apath_arr.length - i].equals(ppath_arr[ppath_arr.length - i])) return false;
		}
		return true;
	}
	
	public static void m2c() {
		for (String m : Helper.tests) {
			String tmp = m.split("\\:")[0];
			Helper.tests_class.add(tmp);
		}
	}
}
