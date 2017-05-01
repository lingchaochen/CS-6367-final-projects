package edu.utdallas.util;

import java.util.zip.CheckedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.CRC32;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Checksum {
	
	private static HashMap<String, Long> PreChecksums = new HashMap<String, Long>();
	private static HashMap<String, Long> CurChecksums = new HashMap<String, Long>();
	
	
	public static void doChecksum(String fileName) {

        try {

            CheckedInputStream cis = null;
            //long fileSize = 0;
            try {
                cis = new CheckedInputStream(
                        new FileInputStream(fileName), new CRC32());

                //fileSize = new File(fileName).length();
                
            } catch (FileNotFoundException e) {
                System.err.println("File not found.");
                System.exit(1);
            }

            byte[] buf = new byte[128];
            while(cis.read(buf) >= 0) {
            }

            long checksum = cis.getChecksum().getValue();
            //System.out.println(checksum + " " + fileSize + " " + fileName);
            Checksum.CurChecksums.put(fileName, checksum);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
	public static ArrayList<String> getAllFilePath(String basePath) {
		ArrayList<String> filePath = new ArrayList<String>();
		File files = new File(basePath);
		File[] tempList = files.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			   if (tempList[i].isFile()) {
				   String[] filename = tempList[i].toString().split("\\.");

				   if (filename.length > 0 && filename[filename.length-1].equals("java")){
					   filePath.add(tempList[i].toString());
					   Checksum.doChecksum(tempList[i].toString());
				   }
			   }
			   if (tempList[i].isDirectory()) {
			    filePath.addAll(Checksum.getAllFilePath(tempList[i].toString()));
			   }
			  }
		return filePath;
	}
	
	public static void writeChecksum() {
		File report = new File(System.getProperty("user.dir") + "/checksums.txt");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(report);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Iterator iter = Checksum.CurChecksums.entrySet().iterator();
        while (iter.hasNext()) {
        	Map.Entry entry = (Map.Entry) iter.next();
        	String key = (String)entry.getKey();
        	long val = (long)entry.getValue();
        	writer.println(key + "|" + val);
        }
		writer.close();
	}
	
	public static int readChecksum() {
		
		HashMap<String, Long> checksums = new HashMap<String, Long>();
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(System.getProperty("user.dir") + "/checksums.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line;
            
            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                String[] checksum = line.split("\\|");
                Checksum.PreChecksums.put(checksum[0], Long.parseLong(checksum[1]));
            }   

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");
        }
        catch(IOException ex) {
            System.out.println("Error reading file");
        }
		
		//System.out.println("CurChecksum: " + Checksum.CurChecksums.size());
		//System.out.println("PreChecksum: " + Checksum.PreChecksums.size());
		return Checksum.PreChecksums.size();
	}
	
	public static ArrayList<String> findChangedClass() {
		
		ArrayList<String> changedClass = new ArrayList<String>();
		
		Iterator curIter = Checksum.CurChecksums.entrySet().iterator();
        while (curIter.hasNext()) {
        	Map.Entry entry = (Map.Entry) curIter.next();
        	String key = (String)entry.getKey();
        	long val = (long)entry.getValue();
        	boolean changed = true;
        	Iterator preIter = Checksum.PreChecksums.entrySet().iterator();
            while (preIter.hasNext()) {
            	Map.Entry preEntry = (Map.Entry) preIter.next();
            	String preKey = (String)preEntry.getKey();
            	long preVal = (long)preEntry.getValue();
            	
            	if (key.equals(preKey) && val == preVal) {
            		changed = false;
            		break;
            	}
            	
            }
            if (changed) changedClass.add(key);
            
        	
        }
        
        for (String c : changedClass) {
        	System.out.println(c + " has been changed.");
        }
		
        System.out.println("In total, " + changedClass.size() + " classes have been changed.");
		
		return changedClass;
	}
}
