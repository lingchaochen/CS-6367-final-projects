package edu.utdallas.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   //TestJunit1.class,
   //TestJunit2.class
})

public class TestSuite {
	
	public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        //suite.addTest(new JUnit4TestAdapter(SampleJUnitTest.class));
        //suite.addTest(new JUnit4TestAdapter(SampleJUnitTest.class));
        return suite;
  }
	
}