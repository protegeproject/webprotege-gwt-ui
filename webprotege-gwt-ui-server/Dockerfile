FROM tomcat:9-jdk11-openjdk

ARG keycloakAdapterUrl=https://github.com/keycloak/keycloak/releases/download/15.0.2/keycloak-oidc-tomcat-adapter-15.0.2.zip

# Download and unzip the keycloak adapter into the tomcat lib directory and then clean up.
# Note that the --location flag makes curl follow redirects
RUN curl --location "${keycloakAdapterUrl}" -o /usr/local/tomcat/lib/kk.zip \
&& unzip /usr/local/tomcat/lib/kk.zip -d /usr/local/tomcat/lib \
&& rm /usr/local/tomcat/lib/kk.zip

COPY ./target/webprotege-gwt-ui-server-*.war /usr/local/tomcat/webapps/webprotege.war

EXPOSE 8080