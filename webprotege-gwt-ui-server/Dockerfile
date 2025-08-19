FROM tomcat:9.0.87-jdk11-corretto-al2

ARG keycloakAdapterUrl=https://github.com/keycloak/keycloak/releases/download/15.0.2/keycloak-oidc-tomcat-adapter-15.0.2.zip

# Download and unzip the keycloak adapter into the tomcat lib directory and then clean up.
# Note that the --location flag makes curl follow redirects
RUN yum -y install unzip
RUN curl --location "${keycloakAdapterUrl}" -o /usr/local/tomcat/lib/kk.zip \
&& unzip /usr/local/tomcat/lib/kk.zip -d /usr/local/tomcat/lib

COPY ./target/webprotege-gwt-ui-server-*.war /usr/local/tomcat/webapps/webprotege.war
COPY logging.properties $CATALINA_HOME/conf/logging.properties
COPY startup.sh /usr/local/bin/startup.sh

# Make startup script executable
RUN chmod +x /usr/local/bin/startup.sh

EXPOSE 8080

# Use the startup script as entrypoint
ENTRYPOINT ["/usr/local/bin/startup.sh"]