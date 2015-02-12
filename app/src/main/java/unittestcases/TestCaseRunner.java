package unittestcases;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author Arnar Jonsson
 * @since 21.11.2014
 * @version 0.1
 *
 */
public class TestCaseRunner 
{
	
	
	  /**
	 * @param args Main, runs the class MyFunctionTest.java
	 */
	public static void main(String[] args) 
	  {
			
	    Result result = JUnitCore.runClasses(MyFunctionTest.class);
	    
	    for (Failure failure : result.getFailures()) 
	    {
	      System.out.println(failure.toString());
	    }
	    
	  }
	
}