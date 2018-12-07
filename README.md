## Log files parser

### Requirements
1. Input. File with name `access.log` in the same directory as where script is launched
2. Database. Script saves date to MySQL database with configuration:
- USER = "root"
- PASSWORD = "mypassword"
- HOST = "localhost"
- PORT = "3306"
- DB = "test"

For testing you can run `docker run --detach --name=test-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" --env="MYSQL_DATABASE=test" --publish 3306:3306 mysql` 

DB schema:


### Build
`./gradlew jar`
Built jar in `build/libs/parser.jar`

### Run
Example:
`java -jar build/libs/parser.jar --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500`

Note: `build/libs/parser.jar` path to built script

## Deliverables

(1) Java program that can be run from command line

(2) Source Code for the Java program

(3) MySQL schema used for the log data

(4) SQL queries for SQL test

