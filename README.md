RED energy ccoding challenge - NEM12 meter reader file validator
================================================================

Author - Tennakoon Arachchige Damith Suranga Tennakoon


Challenge can be found in the file -> README-task-domain-provided.md


Pre-requisites
==============
1. Java 8
2. maven 3

How to build
============
Goto the project directory simplenem12 and type
 mvn clean package
Test cases are attached to maven an will run automatically

How to run
==========
Goto classes folder
 simplenem12/target/classes
Type command below
 java au.com.redchallenge.process.TestHarness ./SimpleNem12.csv

 How to run tests only
 =====================
 Goto the project directory simplenem12 and type
  mvn test

 Following test cases have been covered
 ======================================
 1. Validate empty file
 2. Valid start of file - 100 record
 3. Valid end of file - 900 Record
 4. Valid 200 records
    - Valid NMI
    - Valid eneregy unit
 5. Valid 300 record
    - Valid Quality
    - Valid date


Software and tools used
=======================
1. Java 1.8.0_261-b12
2. maven 3.6.3
3. IntelliJ IDEA 2021.3
4. macOS Big Sur Version 11.3
