#!/bin/bash

# Set default context path if not provided
CONTEXT_PATH=${CONTEXT_PATH:-/webprotege}

# Remove leading slash for directory name
CONTEXT_DIR=$(echo $CONTEXT_PATH | sed 's|^/||')

# Copy the WAR file to the correct location based on context path
if [ "$CONTEXT_PATH" != "/webprotege" ]; then
    # For custom context path, copy WAR to a directory named after the context
    mkdir -p $CATALINA_HOME/webapps/$CONTEXT_DIR
    cp $CATALINA_HOME/webapps/webprotege.war $CATALINA_HOME/webapps/$CONTEXT_DIR/
    # Extract the WAR file
    cd $CATALINA_HOME/webapps/$CONTEXT_DIR
    jar -xf webprotege.war
    rm webprotege.war
fi

# Start Tomcat
exec catalina.sh run
