/*
 Author:       Jose Ortiz Costa
 Date :        10/06/2014
 Modified:     25/03/2016
 Java Version: 8.0
 Program Name: Reflection.java
 Objective:    This program uses command options and argument
               to provide the user a mechanism to explore the 
               internal structure of a class. the following 
               options are used by this class:
               -c: List all the constructors in the class;
               -m: List all the method in the class;
               -v: List all the variables in the class;
               -a: List all the information about the class;
               -C: List all the constants of the class";
               -i: List all the interfaces of the class";
               -h: List help about the options available"
 
*/

import static myUtilities.MyLib.*;
import java.lang.reflect.*;

public class Reflection
{
	String args []; // arguments in command line
	
	/**
	 * Constructor
	 * @param _args arguments from command line
	 */
	public Reflection (String _args [])
	{
		this.args = _args;
		mkWrapper();
	}

   /**
    * Creates a meta-object.
    * @param arg
    * @param is_h
    * @return a meta-object Class <?> 
    */
   
   public Class <?> getClass (String arg, boolean is_h) 
   {
       // This method returns the Class of the argument arg
       Class <?> c = null;
       try 
       {
           c = Class.forName(arg); // get the class
       }
       catch (ClassNotFoundException e)
       {   
           if (!is_h) die("Class not found");
       }
       return c;
   }

   /**
    * Remove the descriptor from the class name e.g: java.lang
    * @param descriptor the complete package from the class
    * @return a String representing the name of the class 
    *         without the descriptor. e.g: return String from
    *         java.lang.String
    */
   public String removeDescriptor(String descriptor)
   {
       String [] parts = descriptor.split("\\.");
       return parts[parts.length-1];
   }

   /**
    * This method gets the parameters without descriptors
    * corresponding to the argument given. Set argument to
    * null if no needed.
    * @param c reflective facility representing the constructor
    * @param m reflective facility representing a method.
    */
   public void printParameters (Constructor <?> c, Method  m)
   {   
       
       int parametersCount = 0;
       Class <?> [] parametersTypes;
       if (m == null)
       {
           parametersTypes = c.getParameterTypes();
           parametersCount = c.getParameterCount();
       }
       else
       {
           parametersTypes = m.getParameterTypes();
           parametersCount = m.getParameterCount();
       } 
       int counter = 1;
       print(" ("); 
       for (Class <?> pt : parametersTypes) 
       {       
           String parameterType = removeDescriptor(pt.getName());
           String primitive = typeForPrimitiveDescriptor(parameterType);
           if (primitive !=null) parameterType = primitive;
           if (counter==parametersCount) print(parameterType);
           else print(parameterType + ",");
           counter++;
       }
       print(")");
      
       nextLine();
   }

   /**
    * Prints Constructors reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_c (Class <?> c)
   {
       
       String mod = "", type = "", name = "";
       Constructor <?> [] co = c.getConstructors(); 
       println("Constructors: ");
       if (co.length == 0) println("No Constructors Found");
       else 
       {
           for (Constructor <?> c1 : co) 
           {   
               c1.setAccessible(true);
               int modifier = c1.getModifiers();
               mod = Modifier.toString(modifier);
               type = c1.toGenericString();
               name = c1.getName();
               String printInfo = classInfo(mod, name);
               print(printInfo);
               printParameters(c1,null);
           }
       }
       nextLine();
   }
  
   /**
    * Prints methods reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_m (Class <?> m)
   {
       // This method prints the method's information
       // found in Class m
       String mod = "";
       String name = "";
       Class <?> type = null;
       Method [] method = m.getDeclaredMethods();
       println("Methods:");
       if (method.length == 0) println("No Methods Found");
       else 
       {
           for (Method mt : method) 
           {   
               mt.setAccessible(true);
               int modifier = mt.getModifiers();
               mod = Modifier.toString(modifier);
               type = mt.getReturnType();
               name = mt.getName();
               String info = classInfo(mod, type,name);
               print(info);
               printParameters(null,mt);
           }
       }
       nextLine();
   }
   
   /**
    * Prints variables reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_v (Class <?> v)
   {
       // This method prints the variables found in Class v
       String mod, name;
       int modifier;
       Class <?> type = null;
       Field[] var = v.getDeclaredFields(); 
       println("Variables: ");
       if (var.length == 0) println("No Variables Found");
       else 
       {
           for (Field f : var) 
           {   
               f.setAccessible(true);
               modifier = f.getModifiers();
               mod = Modifier.toString(modifier);
               type = f.getType();
               name = f.getName();
               String info = classInfo(mod, type,name);
               println(info);
               
           }
       }
       nextLine(); 
   }
   
   /**
    * Prints everything reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_a (Class <?> a)
   {
       // This method prints everything
       opt_c(a); // prints constructors
       opt_m(a); // prints methods
       opt_v(a); // prints variables
       opt_C(a); // prints constants
       opt_i(a); // prints interfaces
   }
   
   /**
    * Prints constants reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_C (Class <?> C)  
   {
       // This method prints all the constants of the Class C
       String mod,name,info;
       Class <?> type;
       
       int modifier = -1;
       Field[] var = C.getDeclaredFields(); // get fields
       println("Constants: ");
       
       for (Field f : var) 
       {
           modifier = f.getModifiers(); // gets modifiers
           // if modifier is static and final, or is
           // only final, print f
           if ((Modifier.isStatic(modifier) && 
                Modifier.isFinal(modifier)) ||
                Modifier.isFinal(modifier)) 
           {
               try
               {   
                   f.setAccessible(true);
                   mod = Modifier.toString(modifier);
                   type = f.getType();
                   name = f.getName();
                   info = classInfo(mod, type, name, f.get(f));
                   println(info);
               }
               catch (IllegalAccessException e)
               {
                   die("Could not access to the value " +
                       "of this Constant");
               }
           }
       }
       if (var.length==0 || modifier == -1)
           println("Constants Not Found");
       nextLine();
   }
   
   /**
    * Prints interfaces reflected from Class c
    * @param c meta-object Class <?>
    */
   public void opt_i (Class <?> i)
   {
       // This method prints all the interfaces of Class i
       int modifier;
       String mod,name,info;
       Class <?> [] interfaces = i.getInterfaces();
       println("Interfaces: ");
       if (interfaces.length == 0) println("No interfaces found");
       else 
       {
           for (Class <?> in : interfaces) 
           {   
               modifier = in.getModifiers(); 
               mod = Modifier.toString(modifier);
               name = in.getName();
               info = classInfo (mod, name); 
               println(info + " ()");  
           }
       }
       nextLine();
   }
   
   /**
    * Gets info about the class passed with its descriptor
    * @param modifier class modifier e.g: public, protected...
    * @param descriptor class descriptor e.g: java.lang.String
    * @return a String with the class info
    */
   public String classInfo(String modifier, String descriptor)
   {
       // This method returns the class info
       String name = removeDescriptor(descriptor);
       return (modifier + " " + name);
   }

   /**
    * Gets info about the class passed with its descriptor
    * @param modifier class modifier e.g: public, protected...
    * @param type  primitives types.
    * @param descriptor class descriptor e.g: java.lang.String
    * @return a String with the class info
    */
   public String classInfo(String modifier, Class <?> type,
                                  String descriptor)
   {
       // This method overload classInfo to return also the type
       String name = removeDescriptor(descriptor);
       String tp = removeDescriptor(type.getName());
       String checkPrimitiveDes = typeForPrimitiveDescriptor(tp);
       if (checkPrimitiveDes != null) tp = checkPrimitiveDes;
       return (modifier + " " + tp + " " + name);
   }
   
   /**
    * Gets info about the class passed with its descriptor
    * @param modifier class modifier e.g: public, protected...
    * @param type  primitives types.
    * @param descriptor class descriptor e.g: java.lang.String
    * @param value types value
    * @return a String with the class info
    */
   public String classInfo(String modifier, Class <?> type,
                                  String descriptor, Object value)
   {
       // This method overloads classInfo to return also the
       // type and the value. Used to get the value of Constants
       String name = removeDescriptor(descriptor);
       String tp = removeDescriptor(type.getName());
       String checkPrimitiveDes = typeForPrimitiveDescriptor(tp);
       if (checkPrimitiveDes != null) tp = checkPrimitiveDes;
       return (modifier + " " + tp + " " + name + " = " + value );
   }

   /**
    * Gets the type for a given primitive descriptor
    * @param prDescriptor primitive encrypted descriptor
    * @return the actual decrypted primitive type
    */
   public String typeForPrimitiveDescriptor(String prDescriptor)
   {
       // This method checks if a type come with a primitive array
       // descriptor and returns it in a more readable way.
       String type = null;
       if (prDescriptor.equals("[C")) type = "[] Char";
       else if (prDescriptor.equals("[B")) type = "[] byte";
       else if (prDescriptor.equals("[D")) type = "[] double";
       else if (prDescriptor.equals("[L")) type = "[] Object";
       else if (prDescriptor.equals("[I")) type = "[] int";
       else if (prDescriptor.equals("[java.lang.String") || 
                prDescriptor.equals("String;")) type = "[] String";
       else if (prDescriptor.equals("[Z")) type = "[] boolean";
       return type; 
   }
   
   /**
    * Prints help options
    */
   public void help ()
   {
       // This method creates the help for option -h
       String header = "Probe Program Options: \n";
       String c = "-c: List all the constructors in the class\n";
       String m = "-m: List all the method in the class\n";
       String v = "-v: List all the variables in the class\n";
       String a = "-a: List all the information about the class\n";
       String C = "-C: List all the constants of the class\n";
       String i = "-i: List all the interfaces of the class\n";
       String h = "-h: List help about the options availables\n";
       println (header + c + m + v + a + C + i + h );
   }
   
   /**
    * Process the options given in command line
    * @param is_c option for reflecting constructors
    * @param is_m option for reflecting methods
    * @param is_v option for reflecting variables
    * @param is_a option for getting info about the class
    * @param is_C option for reflecting constants
    * @param is_i option for reflecting interfaces
    * @param is_h option for printing help
    */
   public void proccesOptions (String arg, boolean is_c, boolean is_m, boolean is_v, 
                               boolean is_a, boolean is_C, boolean is_i, boolean is_h)
   {
       // This method defines which option or options are executed
       Class <?> cl = getClass(arg, is_h); // get Class of arg
       if (is_h) help(); // executes help
       else if (is_a) opt_a(cl); // executes -c, -m, -v, -C, -i 
       else 
       {
           // executes one or multiple options
           if (is_c) opt_c(cl); // -c
           if (is_m) opt_m(cl); // -m
           if (is_v) opt_v(cl); // -v
           if (is_C) opt_C(cl); // -C
           if (is_i) opt_i(cl); // -i
       }       
   }

   /**
    * Makes a given class reflective.
    * @param args command line arguments. 
    */
   public void makeReflective ()
   {
       // This method defines the execution of the options
       // and the argument entered by the user
       GetOpt opt = new GetOpt (this.args, "cmvaCih");
       int counter = 0;
       String arg = "";
       // flag boolean variables 
       boolean is_c = false;
       boolean is_m = false;
       boolean is_v = false; 
       boolean is_a = false;
       boolean is_C = false;
       boolean is_i = false;
       boolean is_h = false;
       // Checks which options have been entered by the user
       while ((counter = opt.getopt()) != -1)
       {
           switch (counter)
           {
               case 'c':  is_c = true; break; 
               case 'm':  is_m = true; break; 
               case 'v':  is_v = true; break; 
               case 'a':  is_a = true; break; 
               case 'C':  is_C = true; break; 
               case 'i':  is_i = true; break; 
               case 'h':  is_h = true; break; 
               default:   die( "Error: Option missing. " + 
                               "Enter -h for help"); break;
           }
       }
       // get argument 
       arg = opt.getarg()[0]; 
       if (arg.equals("") && !is_h) die ("Error: Argument missing");
       // execute the methods given as a argument of this method
       // according to the options entered by the user  
       proccesOptions(arg, is_c, is_m, is_v, 
                      is_a, is_C, is_i, is_h);
   }
}

