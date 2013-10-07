# Makefile
# A very simple makefile for compiling a Java program.  This file compiles
# the sim_cache.java file, and relies on javac to compile all its 
# dependencies automatically.
#
# If you require any special options to be passed to javac, modify the
# CFLAGS variable.  You may want to comment out the DEBUG option before
# running your simulations.

JAVAC = javac
#DEBUG = -g
CFLAGS = $(DEBUG) -deprecation

#Edited to make all required classes first
sim_cache:
	$(JAVAC) $(CFLAGS) Block.java
	$(JAVAC) $(CFLAGS) Set.java
	$(JAVAC) $(CFLAGS) Cache.java
	$(JAVAC) $(CFLAGS) sim_cache.java
	
# type "make clean" to remove all your .class files
clean:
	-rm *.class
