README file for SEE HLA Starter Kit, Version 1.2.0

INTRODUCTION
============
This package contains a Java framework to develop HLA Federates
in the context of the SEE (Simulation Exploration Experience) 
project.

LICENSE
=======
see file License.

FEEDBACK
=======
As you know already, this is still an on-going project. 
We are still working on the framework and new versions will be distributed 
as soon as available.
Your feedback as users is very important to us. Please, if you have new 
requirements that you would like to see implemented or if you have examples 
of usage or if you discover some bugs, send us information.
Check the website https://code.google.com/p/see-hla-starterkit/
for how to report bugs and send suggestions.  

SYSTEM REQUIREMENTS
===================
To build the framework a complete Java programming environment is
needed. At least a Java Development Kit version 1.7 is required. 


KNOWN BUGS
==========
  see https://code.google.com/p/see-hla-starterkit/  ('issues' section)  for the full list of reported bugs


CONTACT
=======
Falcone Alberto, Garro Alfredo - University of Calabria (Italy),
Department of Informatics, Modeling, Electronics, and Systems Engineering (DIMES)
e-mail: {alberto.falcone, alfredo.garro}@dimes.unical.it

INSTALLATION AND TEST
==============================
You can download the SEE HLA Starter Kit in source form and recompile it yourself, 
or get the pre-compiled binaries (actually they are JAR files). 
The following is an excerpt from the programmer's guide.

5.1 Software requirements
=========================
The only software requirement to execute the system is the Java Run Time 
Environment version 1.7
The ANT program to compile the source code of the SEE HLA Starter Kit with 
build.xml file, ANT is available from http://jakarta.apache.org.

5.2 Getting the software
========================
All the software is distributed under the LGPL license limitations. 
It can be downloaded from the SEE HLA Starter Kit web site https://code.google.com/p/see-hla-starterkit/ 
Five compressed files are available:
1. the source code of the SEE HLA Starter Kit
2. the source code of the examples
3. the documentation, including the javadoc of the SEE HLA Starter Kit API
4. the binary of the SEE HLA Starter Kit, i.e. the jar files with all the Java classes
5. a full distribution with all the previous files


5.3 Running the SEE HLA Starter Kit
=============================================
Having uncompressed the archive file, a directory tree is generated whose 
root is SEE_HLA_Starter_Kit and with a lib subdirectory. This subdirectory contains some 
JAR files that have to be added to the CLASSPATH environment variable or in the
eclipse (website: https://www.eclipse.org/home/index.php) build path.

5.3.3 Example
=============
First of all set the CLASSPATH to include the JAR files called 'skf' in the lib 
subdirectory . For instance, for Windows 9x/NT use the following command:
set CLASSPATH=%CLASSPATH%;.;c:\SEE_HLA_Starter_Kit\lib\skf{version_number}.jar;
Or if you use the eclipse IDE you can add this jar in the project build path.
