#!/bin/bash

# FIXME: Specify the location of JaTyC on your host.
JATYC_PATH=$HOME/java-typestate-checker

java -jar $JATYC_PATH/dist/checker/checker.jar -classpath $JATYC_PATH/dist/jatyc.jar -processor jatyc.JavaTypestateChecker *.java

run_server () {
	# Run server for a little while
	timeout 10 java FileServer
}

run_clients () {
	sleep 1 # Give the server some time to initialize
	java FileClient
	java FileClient2
}

# Start the server and client in parallel
run_server & run_clients
