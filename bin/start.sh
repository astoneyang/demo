#!/bin/bash

SCRIPT_HOME="$(dirname "${0}")"
DEMO_HOME="$(cd "${SCRIPT_HOME}/.."; pwd )"
DEMO_LIB_DIR=${DEMO_HOME}/libs
DEMO_CONFIG_DIR=${DEMO_HOME}/config
LOGS_DIR=${DEMO_HOME}/logs

LOG4J_OPT="-Dlogs.dir=$LOGS_DIR -Dlog4j.configuration=file:$DEMO_CONFIG_DIR/log4j.properties"
DEMO_JAR="demo-0.0.1-jar-with-dependencies.jar"

java $LOG4J_OPT -jar $DEMO_LIB_DIR/$DEMO_JAR

echo
echo "demo started with pid $!"
echo

exit 0