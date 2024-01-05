# repo-Team9
Team9 - Stuti Arora, Neha Bhatia, Praful Das, and Arpita Dev


<h1>Project 2 Agile/Scrum: Vote Counting System </h1>

<h2>Note</h2>
All JUnit tests file are in /testing not in /src. Additional .csv files for testing can be found under src/electionFiles.


<h2>Description</h2>

The Vote Counting System is a versatile software tool designed for managing and processing election data for different types of voting systems, such as Instant Runoff (IR), Open Party List (OPL) elections and an additional Multiple Popularity Only (MPO) elections. It reads from structured input files containing election data, processes ballots according to the specified election type, and produces results including winner determination, audit files, and detailed reports of the voting process.


<h2>Table of Contents<h2>

Technologies Used
Requirements
Installation
Usage
Documentation


<h2>Technologies Used</h2>

Java
JUnit for testing
Java I/O for file handling


<h2>Requirements</h2>

Java Development Kit (JDK), version 8 or higher
JUnit for running tests


<h2>Installation</h2>

Clone the repository to your local machine using the following command:

bash
git clone https://github.com/your-username/election-counting-system.git

Navigate into the project directory:

bash
cd election-counting-system

Compile the project using:
javac *.java


<h2>Usage</h2>

To run the program, execute the following command:

java Controller

Follow the prompts in the command line interface to input the necessary commands for processing the election data file.


<h3>Input File Format</h3>
Ensure that the election data file follows the required CSV format as detailed in the project documentation. The files should be in the src/electionFiles directory.

<h3>Audit Files</h3>
The corresponding audit files should be generated in the src/auditFiles/ directory. 


<h3>Commands</h3>

<yourfilename>.csv: Process an election data file with the specified file name.
test: Enter the testing environment.
exit: Quit the program.


<h2>Documentation</h2>

The source code is extensively documented using Javadoc comments. To generate the formal documentation, run the following command:

javadoc -d doc *.java

Navigate to /Project1/documentation/ to view the generated HTML documentation.
