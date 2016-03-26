/*
 
 Author: Jose Ortiz Costa 
 Date  : 09/30/2014
 Java Version: 8.0
 Program Name: myLib.java
 Objective: This library is packaged in the package 
            myUtitlies, and provides many useful methods for
            faster development of java applications.
*/

package myUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.util.*;

public class MyLib 
{

//*********************println()*********************************
   public static void println (Object...o)
   {
       // Prints each argument 
       for (int i = 0; i<o.length; i++)
       {
           System.out.println(o[i] + " ");
       }
        
   }

//*********************print()***********************************
   public static void print (Object...o)
   {
       // Prints in the same line
       for (int i = 0; i<o.length; i++)
       {
           System.out.print(o[i]);
       }
        
   }

//*********************die()**************************************
   public static void die (String ... msg) 
   {
       // Terminates the program displaying a error message
       if (msg.length == 0) System.exit(1);
       System.err.println(msg[0]);
       System.exit(1);
   }

//********************nextLine()**********************************
   public static void nextLine()
   {
       // Jumps to the next Line
       println(" ");   
   }


//*********************printArrayContents()***********************
   public static <T> void printArrayContents( ArrayList <T> list)
   {
       for (T object : list) println(object);
           
   }

//*********************printArrayContents()***********************
   public static void printArrayContents ( String [] list)
   {
       for (String object : list) println (object);
   }
  
 //********************clearConsole()*****************************
   public static void clearConsole () 
   {
       // Clear console screen
       String CLS     = "\033[2J";
       String GOTO1_1 = "\033[1;1H";
       print(CLS);
       print(GOTO1_1);
       
   }
//********************getScreenHeight()***************************
   public static int getScreenHeight()
   {
       // Get the height of the screen
       Toolkit tk  = Toolkit.getDefaultToolkit();
       Dimension d = tk.getScreenSize();
       return d.height;
       
   }
//********************getScreenWidth******************************
   public static int getScreenWidth()
   {
       // Get the width of the screen
       Toolkit tk  = Toolkit.getDefaultToolkit();
       Dimension d = tk.getScreenSize();
       return d.width;
       
   }
//********************locate()************************************
   public static void locate(int row, int col)
   {
       // locates a position in screen 
       print("\033["+row+";"+col+"H");
   } 

 //*********************mkWrapper()********************************
    public static void mkWrapper()
    {
        // This method, once exucuted, provides a mechanism for
        // executing a java program using only the name of the
        // program instead of using the word java followed by the
        // name of the program  
        String s;
        s = Thread.currentThread().getStackTrace()[1].getClassName();
        String wrapperName = s;
        File f = new File(wrapperName);
        if(!f.exists())
        {
            try
            {
                PrintWriter pw = new PrintWriter(f);
                pw.println("#!/bin/bash");
                pw.println("export PATH=$PATH:.");
                pw.println("export CLASSPATH=.:$CLASSPATH");
                pw.println("java $(basename $0) ${1+\"$@\"}");
                pw.close();
            }catch(FileNotFoundException e){System.err.println(e);}
        }
        try
        { 
            Process p;
            p = new ProcessBuilder("chmod", 
                                  "+x", 
                                  f.getAbsolutePath()).start();
           
        }   catch(IOException e){System.err.println(e);}
    }
}
