
/*
   @author:      Jose Ortiz
   Date:         03/25/2016
   Description:  Test Class Reflection
   @see          Reflection.java
   Usage:        This example uses the java.lang.String 
                 to run the program 
   Note:         You can run this program using multiple 
                 options at the same time
   Compile:      javac ReflectionTest.java
   Run:          java ReflectionTest -m -v java.lang.String
 
   Output:       All the methods and variables reflected from java.lang.String class
*/


public class ReflectionTest 
{
    public static void main (String args [])
    {
	    Reflection reflect = new Reflection (args);
		// reflects from the given class in args 
		// using the chosen options
		reflect.makeReflective(); 	
    }		
}