# JavaReflection
This program will help you to reflect in any Java class including your own classes by using Reflection. 
# Usage
This program uses command options and argument to provide the user a mechanism to explore the internal structure of a class. the following  options are used by this class: <br \>
<strong> -c: List all the constructors in the class </strong> <br \> 
<strong> -m: List all the method in the class </strong> <br \>
<strong> -v: List all the variables in the class </strong> <br \>
<strong> -a: List all the information about the class </strong> <br \>
<strong> -C: List all the constants of the class </strong> <br \>
<strong> -i: List all the interfaces of the class </strong> <br \>
<strong> -h: List help about the options available </strong> <br \>
# Compilation
Either you can use the ReflectionTest program provided here as a test for the Reflection class, or you can make your own main program. <br \>
For this example we are using the provided ReflectionTest.java class for compilation and execution. 
Note: The whole project including Reflection.java, myUtilities package and GetOpt.class must be on the same directory where the compilation takes place. <br \>
You can compile this program in terminal by using this command: <br \>
<strong> javac ReflectionTest.java </strong>
# Execution
For this example we are reflecting over the classes java.util.Date, but you can use any class made in Java including your own classes. The following command will output all the reflective facilities such as constructors, methods, variables. constants, interfaces as well as info about java.util.Date class. <br \>
```terminal
    java ReflectionTest -c -m -v -a -C -i -h java.util.Date </strong>
```

