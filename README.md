mvn clean install
mvn spring-boot:run
http://localhost:8081/swagger-ui/index.html#/

`mvn flyway:migrate -DDB_HOST=your-host -DDB_PORT=your-port -DDB_NAME=db_gpu -DDATABASE_USER=you -DDATABASE_PASSWORD=password`