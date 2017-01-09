#####################################################################
# Makefile:
#    This is a makefile for maintaining and running the SISO Space
# Simulation Smackdown Java Aitken Basin federate.
#
#####################################################################
# Creation:
#    Author: Edwin Z. Crues
#    Date:   March 2015
#
#####################################################################
#
# To get a desription of the arguments accepted by this makefile,
# type 'make help'
#
#####################################################################
# Copyright 2015 Edwin Z. Crues
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.
#####################################################################

PROGRAM = AitkenBasin

JAR_FILE = lib/$(PROGRAM).jar

MANIFEST = Manifest.txt

RTI_VENDOR ?= Pitch

#####################################################################
##                      FOM FILE DEFINITIONS                       ##
#####################################################################
FOM_DIR_PATH = ../../../FOMs
FOM_FILES = $(FOM_DIR_PATH)/SISO_SpaceFOM_core.xml\
            $(FOM_DIR_PATH)/SISO_SpaceFOM_environ.xml

#####################################################################
##                    ENVIRONMENT DEFINITIONS                      ##
#####################################################################
ENV_HOME ?= ../Environment
ENV_JAR_PATH = ${ENV_HOME}/lib
ENV_CLASS_PATH = $(ENV_JAR_PATH)/Environment.jar

#####################################################################
##                         JAT DEFINITIONS                         ##
#####################################################################
JAT_HOME ?= ../Environment/jat
JAT_JAR_PATH = ${JAT_HOME}/lib
JAT_CLASS_PATH = $(JAT_JAR_PATH)/jatcore.jar:$(JAT_JAR_PATH)/jatcoreNOSA.jar

#####################################################################
##                        JARGS DEFINITIONS                        ##
#####################################################################
JARGS_HOME ?= ../Environment
JARGS_JAR_PATH = ${JARGS_HOME}/lib
JARGS_CLASS_PATH = $(JARGS_JAR_PATH)/jargs.jar

#####################################################################
##                         RTI DEFINITIONS                         ##
#####################################################################

ifeq ($(RTI_VENDOR),Mak)
RTI_LIB_PATH   = ${RTI_HOME}/lib
RTI_JAR_FILE   = hla.jar
else
RTI_LIB_PATH   = ${RTI_HOME}/lib
RTI_JAR_FILE   = prti1516e.jar
endif
RTI_JAR_LINK = ./lib/rti1516e.jar
RTI_CLASS_PATH = $(RTI_JAR_LINK)

#####################################################################
##                      DIRECTORY DEFINITIONS                      ##
#####################################################################

PACKAGE_PATH = see/smackdown

AITKEN_BASIN_SRC_PATH = src/$(PACKAGE_PATH)/aitken_basin
AITKEN_BASIN_BIN_PATH = bin/$(PACKAGE_PATH)/aitken_basin


#####################################################################
##                      COMPILER DEFINITIONS                       ##
#####################################################################

#
# JAVA Compilation Variables
#
JAVA_OPTS = -Xlint:unchecked -classpath $(ENV_CLASS_PATH):$(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(RTI_CLASS_PATH) -sourcepath src
JAVADOC_OPTS = -classpath $(ENV_CLASS_PATH):$(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(RTI_CLASS_PATH) -sourcepath src


#####################################################################
##                     FILE NAME DEFINITIONS                       ##
#####################################################################

AITKEN_BASIN_SRC = $(wildcard $(AITKEN_BASIN_SRC_PATH)/*.java)
AITKEN_BASIN_OBJ = $(addprefix $(AITKEN_BASIN_BIN_PATH)/,$(notdir $(subst .java,.class,$(AITKEN_BASIN_SRC))))


#####################################################################
##                        PROGRAM TARGETS                          ##
#####################################################################

default: jar

run: $(JAR_FILE) links
	 java -cp $(RTI_CLASS_PATH):$(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(JAR_FILE) see.smackdown.aitken_basin.AitkenBasin

docs: java_docs

java_docs: 
	javadoc $(JAVADOC_OPTS) -d docs/java $(AITKEN_BASIN_SRC)

jar: $(RTI_JAR_LINK) AitkenBasin
	jar cfm $(JAR_FILE) $(MANIFEST) -C bin see
	@ echo "Created jar file: $(JAR_FILE)"

links: $(FOM_FILES) $(RTI_JAR_LINK)

$(JAR_FILE):
	make jar

#
# Model subpackage targets.
#
AitkenBasin: $(AITKEN_BASIN_BIN_PATH) $(AITKEN_BASIN_OBJ)
	@ echo "Building the Aitken Basin classes"


#
# Make sure that the bin directories exist.
#
$(AITKEN_BASIN_BIN_PATH):
	@ mkdir -p $(AITKEN_BASIN_BIN_PATH)
	@ echo "Created $(AITKEN_BASIN_BIN_PATH)"


#
# Compilation directives.
#
$(AITKEN_BASIN_OBJ): $(AITKEN_BASIN_BIN_PATH)/%.class : $(AITKEN_BASIN_SRC_PATH)/%.java
	javac $(JAVA_OPTS) -d bin $(AITKEN_BASIN_SRC_PATH)/${<F}


#
# Check for FOM files.
#
$(FOM_FILES):
	@ echo "Cannot locate FOM modules in: $(FOM_DIR_PATH)"
	exit 1


#
# Check for link to RTI JAR file.
#
$(RTI_JAR_LINK): $(RTI_LIB_PATH)/$(RTI_JAR_FILE)
	@ echo "Creating link to RTI JAR file: $(RTI_LIB_PATH)/$(RTI_JAR_FILE)"
	ln -s $(RTI_LIB_PATH)/$(RTI_JAR_FILE) lib/rti1516e.jar


#############################################################################
##                         MAINTENANCE TARGETS                             ##
#############################################################################

real_clean: clean clean_RTI clean_docs
	$(RM) -rf bin $(JAR_FILE)
	@ echo "Removed the entire bin directory!"

clean: clean_aitken_basin
	@ echo "All class files removed"

clean_docs: clean_java_docs
	@ echo "Removed all documentation."

clean_java_docs:
	@ $(RM) -rf docs/java
	@ echo "Removed Java documentation."

clean_RTI:
	$(RM) $(RTI_JAR_LINK)
	@ echo "Removed link to RTI jar file."

clean_aitken_basin:
	$(RM) -f $(AITKEN_BASIN_BIN_PATH)/*.class
	@ echo "Aitken Basin class files removed"

help :
	@ echo "\n\
Make Options:\n\
   make            - Compiles are Java source files and makes the JAR file\n\
\n\
   make run        - Builds necessary files and runs the application\n\
\n\
   make clean      - Deletes the class files\n\
\n\
   make clean_docs - Deletes the generated documentation files\n\
\n\
   make real_clean - Deletes all make generated files and directories\n"

