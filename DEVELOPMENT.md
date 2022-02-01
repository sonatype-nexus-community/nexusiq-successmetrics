## Development

Should you wish to contribute to the project:

- Fork this repository
- Make and test your changes in your forked repository
- It is strongly recommended that you use pre-commit actions, see [pre-commit.com](https://pre-commit.com)
- Write tests for your changes and confirm that the tests pass
- When you are ready to raise a PR then rebase your code based on the main branch of this repo
- (Preferably) Squash all your commits in your forked repository
- Submit a pull request to this project

To run tests:

```
./gradlew test
```

To build:

```
./gradlew clean build
```

To run the application:

```
./gradlew bootRun
```

To run from the JAR file:
java -jar success-metrics-<version>.jar

To scan with Nexus IQ:

```
./gradlew nexusIQScan -Pusername=<username> -Ppassword=<password> -PserverUrl=<server url:port>
```

... for example:

```
./gradlew nexusIQScan -Pusername=admin -Ppassword=admin123 -PserverUrl=http://localhost:8070
```
