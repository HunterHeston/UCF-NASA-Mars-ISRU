#####################################################################
# Makefile:
#    This is a makefile for maintaining and running the SISO Space
# Simulation Smackdown Java Easy Button Federate.
#
#####################################################################
# Creation:
#    Author: Edwin Z. Crues
#    Date:   February 2011
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

PROGRAM = Environment

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
##                         JAT DEFINITIONS                         ##
#####################################################################
JAT_HOME ?= ./jat
JAT_JAR_PATH = ${JAT_HOME}/lib
JAT_CLASS_PATH = $(JAT_JAR_PATH)/jatcore.jar:$(JAT_JAR_PATH)/jatcoreNOSA.jar

#####################################################################
##                        JARGS DEFINITIONS                        ##
#####################################################################
JARGS_HOME ?= .
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

ENVIRONMENT_SRC_PATH = src/$(PACKAGE_PATH)/environment
ENVIRONMENT_BIN_PATH = bin/$(PACKAGE_PATH)/environment

REF_FRAME_SRC_PATH = src/$(PACKAGE_PATH)/reference_frame
REF_FRAME_BIN_PATH = bin/$(PACKAGE_PATH)/reference_frame

UTILITIES_SRC_PATH = src/$(PACKAGE_PATH)/utilities
UTILITIES_BIN_PATH = bin/$(PACKAGE_PATH)/utilities

#####################################################################
##                      COMPILER DEFINITIONS                       ##
#####################################################################

#
# JAVA Compilation Variables
#
JAVA_OPTS = -Xlint:unchecked -classpath $(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(RTI_CLASS_PATH) -sourcepath src
JAVADOC_OPTS = -classpath $(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(RTI_CLASS_PATH) -sourcepath src


#####################################################################
##                     FILE NAME DEFINITIONS                       ##
#####################################################################

ENVIRONMENT_SRC = $(wildcard $(ENVIRONMENT_SRC_PATH)/*.java)
ENVIRONMENT_OBJ = $(addprefix $(ENVIRONMENT_BIN_PATH)/,$(notdir $(subst .java,.class,$(ENVIRONMENT_SRC))))

REF_FRAME_SRC = $(wildcard $(REF_FRAME_SRC_PATH)/*.java)
REF_FRAME_OBJ = $(addprefix $(REF_FRAME_BIN_PATH)/,$(notdir $(subst .java,.class,$(REF_FRAME_SRC))))

UTILITIES_SRC = $(wildcard $(UTILITIES_SRC_PATH)/*.java)
UTILITIES_OBJ = $(addprefix $(UTILITIES_BIN_PATH)/,$(notdir $(subst .java,.class,$(UTILITIES_SRC))))

#####################################################################
##                        PROGRAM TARGETS                          ##
#####################################################################

default: jar

run: $(JAR_FILE) links
	 java -cp $(RTI_CLASS_PATH):$(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(JAR_FILE) see.smackdown.environment.Environment

test: $(JAR_FILE) links
	 java -cp $(RTI_CLASS_PATH):$(JARGS_CLASS_PATH):$(JAT_CLASS_PATH):$(JAR_FILE) see.smackdown.environment.EnvironmentTest

docs: java_docs

java_docs: 
	javadoc $(JAVADOC_OPTS) -d docs/java $(ENVIRONMENT_SRC) $(REF_FRAME_SRC) $(UTILITIES_SRC)

jar: $(RTI_JAR_LINK) Environment
	jar cfm $(JAR_FILE) $(MANIFEST) -C bin see
	@ echo "Created jar file: $(JAR_FILE)"

links: $(FOM_FILES) $(RTI_JAR_LINK)

$(JAR_FILE):
	make jar

#
# Model subpackage targets.
#
Environment: $(ENVIRONMENT_BIN_PATH) $(ENVIRONMENT_OBJ)
	@ echo "Building the Environment classes"

RefFrames: $(REF_FRAME_BIN_PATH) $(REF_FRAME_OBJ)
	@ echo "Building the ReferenceFrame classes"

Utils: $(UTILITIES_BIN_PATH) $(UTILTIES_OBJ)
	@ echo "Building the Utilities classes"

#
# Make sure that the bin directories exist.
#
$(ENVIRONMENT_BIN_PATH):
	@ mkdir -p $(ENVIRONMENT_BIN_PATH)
	@ echo "Created $(ENVIRONMENT_BIN_PATH)"

$(REF_FRAME_BIN_PATH):
	@ mkdir -p $(REF_FRAME_BIN_PATH)
	@ echo "Created $(REF_FRAME_BIN_PATH)"

$(UTILITIES_BIN_PATH):
	@ mkdir -p $(UTILITIES_BIN_PATH)
	@ echo "Created $(UTILITIES_BIN_PATH)"

#
# Compilation directives.
#
$(ENVIRONMENT_OBJ): $(ENVIRONMENT_BIN_PATH)/%.class : $(ENVIRONMENT_SRC_PATH)/%.java
	javac $(JAVA_OPTS) -d bin $(ENVIRONMENT_SRC_PATH)/${<F}

$(REF_FRAME_OBJ): $(REF_FRAME_BIN_PATH)/%.class : $(REF_FRAME_SRC_PATH)/%.java
	javac $(JAVA_OPTS) -d bin $(REF_FRAME_SRC_PATH)/${<F}

$(UTILITIES_OBJ): $(UTILITIES_BIN_PATH)/%.class : $(UTILITIES_SRC_PATH)/%.java
	javac $(JAVA_OPTS) -d bin $(UTILITIES_SRC_PATH)/${<F}

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

clean: clean_env clean_ref_frame clean_utils
	@ echo "All class files removed"

clean_docs: clean_java_docs clean_tex_docs
	@ echo "Removed all documentation."

clean_java_docs:
	@ $(RM) -rf docs/java
	@ echo "Removed Java documentation."

clean_tex_docs:
	@ $(RM) -rf docs/src/*.aux docs/src/*.log docs/src/*.dvi
	@ $(RM) -rf docs/src/*.nav docs/src/*.out docs/src/*.snm
	@ $(RM) -rf docs/src/*.toc docs/src/*.vrb
	@ $(RM) -rf docs/src/Environment.pdf
	@ $(RM) -rf docs/src/EnvironmentSlides.pdf
	@ echo "Removed LaTeX documentation."

clean_RTI:
	$(RM) $(RTI_JAR_LINK)
	@ echo "Removed link to RTI jar file."

clean_env:
	$(RM) -f $(ENVIRONMENT_BIN_PATH)/*.class
	@ echo "Environment class files removed"

clean_ref_frame:
	$(RM) -f $(REF_FRAME_BIN_PATH)/*.class
	@ echo "ReferenceFrame class files removed"

clean_utils:
	$(RM) -f $(UTILITIES_BIN_PATH)/*.class
	@ echo "Utilities class files removed"

help :
	@ echo "\n\
Make Options:\n\
   make            - Compiles are Java source files and makes the JAR file\n\
\n\
   make run        - Builds necessary files and runs the application\n\
\n\
   make test       - Builds necessary files and runs the test application\n\
\n\
   make clean      - Deletes the class files\n\
\n\
   make clean_docs - Deletes the generated documentation files\n\
\n\
   make real_clean - Deletes all make generated files and directories\n"

