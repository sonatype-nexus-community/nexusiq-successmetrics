# Development

Should you wish to contribute to the project:

- Fork this repository
- Make and test your changes in your forked repository
- It is strongly recommended that you use pre-commit actions, see [pre-commit.com](https://pre-commit.com)
- Write tests for your changes and confirm that the tests pass
- When you are ready to raise a PR then rebase your code based on the main branch of this repo
- (Preferably) Squash all your commits in your forked repository
- Submit a pull request to this project

## To run tests

```bash
./gradlew test
```

## To build

```bash
./gradlew clean build
```

## To run the applications individually

```bash
./gradlew get-metrics:bootRun
./gradlew view-metrics:bootRun
```

## To scan with Nexus IQ

```bash
./gradlew nexusIQScan -PnexusIQ.username=<username> -PnexusIQ.password=<password> -PnexusIQ.serverUrl=<server url:port> -PnexusIQ.stage=build
```

... for example:

```bash
./gradlew nexusIQScan -PnexusIQ.username=admin -PnexusIQ.password=admin123 -PnexusIQ.serverUrl=http://localhost:8070 -PnexusIQ.stage=build
```

Defaults are:
| Parameter | Default |
|-|-|
| nexusIQ.username | admin |
| nexusIQ.password | admin123 |
| nexusIQ.serverUrl | <http://localhost:8070> |
| nexusIQ.stage | build |
