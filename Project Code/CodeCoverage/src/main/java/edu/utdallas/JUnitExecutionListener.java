package edu.utdallas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import edu.utdallas.util.Checksum;
import edu.utdallas.util.Helper;

public class JUnitExecutionListener extends RunListener {
	
	@Override
	public void testStarted(Description description) throws Exception {
        Helper.testPath = description.getClassName() + ":" + description.getMethodName();
        Helper.hashmap.put(Helper.testPath, new HashSet<String>());
        //this.testIgnored(description);
    }
	
	@Override
	public void	testIgnored(Description description) throws Exception {
		//System.out.print("ignore");
		super.testIgnored(description);
	}
	
	public void testRunStarted(Description description) throws Exception {
		ArrayList<String> filepath = Checksum.getAllFilePath(System.getProperty("user.dir"));
		int preChecksumNum = Checksum.readChecksum();
		if (preChecksumNum == 0) {
			
		}
		
        Checksum.writeChecksum();
        //boolean a = Helper.isSameClass("/Users/jiangong/Desktop/testing/commons-dbutils-trunk/src/main/java/org/apache/commons/dbutils/handlers/columns/DoubleColumnHandler.java", "org.apache.commons.dbutils.handlers.columns.DoubleColumnHandler");
        
        //System.out.println("is the same: " + a);
        Helper.readTestClassRelationship();
        Helper.findTests(Checksum.findChangedClass());
        
        Helper.m2c();
        String content = "import org.junit.runner.RunWith;\n"
                + "import org.junit.runners.Suite;\n@RunWith(Suite.class)\n@Suite.SuiteClasses({";
        for(String test : Helper.tests_class)
            content += test + ".class,\n";
        content += "})\n";
        content += "public class SelectedTestSuite{\n}";    
        writeSuitFile(System.getProperty("user.dir")+"/src/test/java/SelectedTestSuite.java", content);

        
        System.out.println("In total, " + Helper.tests.size() + " tests need to be executed.");
	}
	
	@Override
	public void testRunFinished(Result result) throws Exception {
		// TODO Auto-generated method stub

		super.testRunFinished(result);
		
		Helper.writeTestClassRelationship();

        File report = new File(System.getProperty("user.dir") + "/stmt-cov.txt");
        //System.out.println("path: " + System.getProperty("user.dir") + "/stmt-cov.txt");

        /*
        System.out.println("file amount: " + filepath.size());
        
        for (String file : filepath) {
        	System.out.println(file);
        }
        */
        
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(report);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        System.out.println("Statement coverage information is written to stmt-cov.txt file in the root directory");
        //writer.println("Each Statement that has been covered by the Test is listed below as well as its path and class name for each Methods: ");
        
        Iterator iter = Helper.hashmap.entrySet().iterator();
        while (iter.hasNext()) {
        	Map.Entry entry = (Map.Entry) iter.next();
        	String key = (String)entry.getKey();
        	HashSet<String> val = (HashSet<String>)entry.getValue();
        	writer.println("[TEST] " + key);
        	//Helper.totalLines++;
        	
        	for (String info : val) {
        		writer.println(info);
        		//Helper.totalLines++;
        	}
        }
        
        writer.close();
        
       // System.out.println("Total Executed Lines: " + Helper.totalLines);
        
	}
	
	public void writeSuitFile(String fileName, String content){
        File file = new File(fileName);
        
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            byte[] contentInBytes = content.getBytes();
            
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
