Graph ADT default is for directed graphs as it will read in arcs.

Files
AnalyzeGraph.		java	main class
Graph.java		interface
GraphADT.java		class
DLLDynamicSet.java	class to be used by GraphADT
DynamicSet.java		interface for DLLDynamicSet
TestGraphADT.		java	jUnit test

How to run AnalyzeGraph.java in command line
1. Start -> Run and type cmd
	or search for Terminal on Mac
2. Navigate to where the ics311 folder is located.
	dir	available directories
	cd	to change directory
	e.x.	C:\Users\Name>cd desktop
		C:\Users\Name\Desktop>cd "Project 3"
3. Compile all the files
	 e.x. 	C:\Users\Name\Desktop\Project 3
		C:\Users\Name\Desktop\Project 3>javac ics311\*.java
4. To pass in .vna graph file for analysis, put the file in the same ics311 folder
5. Pass in the file location when running the AnalyzeGraph main class
	e.x.	C:\Users\Name\Desktop\Project 3>java ics311/AnalyzeGraph ics311/celegansneural.vna
6. Information will be printed


Optional how to run TestGraphADT.java in command line
(Download jUnit if needed)
1  Set your CLASSPATH	http://junit.sourceforge.net/doc/faq/faq.htm#running_1
2.  Invoke the runner: by typing the command in step 3
3.  java org.junit.runner.JUnitCore <test class name>
	For jUnit 4.x java -cp /usr/share/java/junit.jar org.junit.runner.JUnitCore [test class name]
Alternatively, Eclipse can be downloaded to run jUnit by creating files and replacing with the files in ics311 package

To use code, change the package or reuse the package.
Create a class if needed for IDE and replace with code using
Operations and methods are specified in Graph.java

JDK 1.7

Log Changes:
5.2.14	Implementations finished

Bug Report:
No bugs