web:	java $JAVA_OPTS -Ddw.http.port=$PORT \
-Ddw.http.adminPort=$PORT \
-Ddw.databaseConfiguration.user=$DB_USER
-Ddw.databaseConfiguration.password=$DB_PASSWORD
-Ddw.databaseConfiguration.url=jdbc:postgresql:$DB_HOST:$DB_PORT/$DB_NAME
-jar dropwizard_learning/target/dropwizard-1.0-SNAPSHOT.jar server dropwizard_learning/libraryService.yml