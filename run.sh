#!/bin/bash

#java -jar ../jatyc/checker/checker.jar -classpath ../jatyc/jatyc.jar -processor jatyc.JavaTypestateChecker *.java
java -jar ~/java-typestate-checker/dist/checker/checker.jar -classpath ~/java-typestate-checker/dist/jatyc.jar -processor jatyc.JavaTypestateChecker *.java
