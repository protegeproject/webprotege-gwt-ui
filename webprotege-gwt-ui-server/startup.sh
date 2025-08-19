#!/bin/bash

# Set default context path if not provided
CONTEXT_PATH=${CONTEXT_PATH:-/webprotege}

# Create the context.xml file dynamically
cat > $CATALINA_HOME/conf/Catalina/localhost/context.xml << EOF
<Context path="${CONTEXT_PATH}">
    <Valve className="org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve"/>
</Context>
EOF

# Start Tomcat
exec catalina.sh run
