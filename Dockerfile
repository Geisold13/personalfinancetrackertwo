FROM openjdk:21

WORKDIR /app

COPY ./target/personal-finance-tracker-two-0.0.1-SNAPSHOT.jar app.jar

COPY global-bundle.pem /tmp/global-bundle.pem

RUN keytool -import -trustcacerts -alias rds-root \
  -file /tmp/global-bundle.pem \
  -keystore $JAVA_HOME/lib/security/cacerts \
  -storepass changeit -noprompt

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]