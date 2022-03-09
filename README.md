![Coverage](.github/badges/jacoco.svg)
# Success Metrics Application for IQ Server

IQ Server has a number of REST APIs, one of which is the [Success Metrics REST API](https://help.sonatype.com/iqserver/automating/rest-apis/success-metrics-data-rest-api---v2) which retuns policy evaluation, violation and remediation data in csv or JSON format. The Customer Success team at Sonatype have created this Success Metrics Application to give a better visual representation of the aggregated data. This provides you with outcome-based metrics which can be measured over a period of time and help drive desired behvaiour change towards agreed goals.

Using the Success Metrics application is a two step process:
1. Extract metrics from IQ server via REST API
2. Run the Success Metrics application against the success metrics csv

# Installation

  * From the Releases pane on the right side of this page select the latest release
  * Click on the *successmetrics-[releasenumber].zip* file on the assets page to download and save to your machine
  * Unzip the contents into a directory of your choice
  * Navigate to the *successmetrics-[releasenumber]* directory (this will be your working directory)

```
unzip successmetrics-[releasenumber].zip
cd successmetrics-[releasenumber]
```

# 1. Extract Metrics from the IQ Server REST API

To use the Success Metrics application you must first have an extract of raw success metric data from your IQ Server by using the [Success Metrics REST API](https://help.sonatype.com/iqserver/automating/rest-apis/success-metrics-data-rest-api---v2). To help with this we have included a **create-data** script in the root directory which will retrieve success metric data from your IQ Server.

## Configure Metrics Extract Time Periods

The [Success Metrics REST API](https://help.sonatype.com/iqserver/automating/rest-apis/success-metrics-data-rest-api---v2) requires a JSON payload request body containing values related to the time period (weekly or monthly) and the start and end dates of the data you wish to extract. You will find two examples in the root *successmetrics-[releasenumber]* directory called *monthly.json* and *weekly.json*.

 * Edit either the included *monthly.json* or *weekly.json* file to adjust the firstTimePeriod (the week or month to start reporting from) and optionally add an end period

 Example: the following request body will fetch data for all organisations and applications between Jan 2020 and Sept 2021 on a monthly basis:
 ``` 
 {
  "timePeriod": "MONTH",
  "firstTimePeriod": "2020-01",
  "lastTimePeriod": "2021-09",
  "applicationIds": [],
  "organizationIds": []
 }
 ```

### Please Note:

 * For large installations/datasets, we recommend limiting the extract to a smaller period (e.g. previous 6 months or weeks) or subset of organisations and/or applications. For information on how to include specific application or organization IDs please see the [Success Metrics REST API](https://help.sonatype.com/iqserver/automating/rest-apis/success-metrics-data-rest-api---v2) page for more details.

 * A minimum of three data points (weeks or months) are needed to display graphs in the Success Metrics web UI. 
 
 * Only fully completed months (or weeks) are included in the data extract, so in the example above, the script will collect monthly data from January 2020 until September 2021 if the extract had been executed from October 2021 onwards.

## Run the Metrics Extract

The following **create-data** script will create a file called **successmetrics.csv** in the root *successmetrics-[releasenumber]* directory, this will be consumed by the success metrics application upon startup. We suggest opening the file to ensure it contains metrics data as expected once the script has completed.

```
Windows: create-data.bat <iq-host-url> <iq-username> <iq-password> <time-period-json>
Linux: create-data.sh <iq-host-url> <iq-username> <iq-password> <time-period-json>

iq-host-url - your Nexus IQ URL (note: do not add a trailing forward slash)
iq-username - your Nexus IQ user name (for data on all applications use an ID with the 'Policy Administrator' role)
iq-password - your Nexus IQ password
time-period-json - weekly.json or monthly.json (see the section above for more information)

Example (Windows):  create-data.bat http://localhost:8070 admin admin123 monthly.json
```

### (Optional) Additional Metrics

For more advanced metrics on Quarantined components (Nexus Reposository v3 & Nexus Firewall required), Waivers and Application last scan dates we have Python scripts which utilise additional REST APIs to aggregate more data. The additional metrics can be created by executing the **create-data** script within the directory *successmetrics-[releasenumber]/reports2/*

Please note: Python3 is required

```
cd reports2
Windows: create-data.bat <iq-host-url> <iq-username> <iq-password>
Linux: create-data.sh <iq-host-url> <iq-username> <iq-password>

iq-host-url - your Nexus IQ URL (note: do not add a trailing forward slash)
iq-username - your Nexus IQ user name (for data on all applications use an ID with the 'Policy Administrator' role)
iq-password - your Nexus IQ password

Example (Windows):  create-data.bat http://localhost:8070 admin admin123 monthly.json
```

The resulting data files will be created within the *successmetrics-[releasenumber]/reports2/* directory, these will be used by the success metrics application when it starts up.


# 2. Run the Success Metrics Application

The Success Metrics application is a Java application which can be run from the command line, via our shell wrapper script or inside a Docker Container using our Docker image. 

There are two modes to the application;
1. Web mode (interactive)
2. Data extract mode (non-interactive)

The application mode should be set in the `./successmetrics-[releasenumber]/application.properties`

```
spring.profiles.active=web
```
```
spring.profiles.active=data
```

### Web Mode
This mode will start up the Web UI for the Success Metrics application on the localhost http://localhost:4040

The Web UI will display the provided success metrics through various charts.

### Data Extract Mode
The Success Metrics application will perform calculations on the provided metrics, output files representing this and then close. Three output files can then be found in the `./successmetrics-[releasenumber]/output` directory.
* Metrics Summary pdf
* Insights csv
* Data Extract csv

### Prerequisites 
When the Success Metrics application starts there must be a `successmetrics.csv` present containing the data you wish to process, see the previous section of this readme for more details on this.

## OPTION A - Start using the shell wrapper script

Execute the `runapp.bat` script from the working directory `./successmetrics-[releasenumber]/`

```
Windows: runapp.bat
Linux: sh runapp.bat
```
Depending on which run mode you selected you will then have either a webserver running or data extract output into the output directory.

## OPTION B - Start using the Docker Image

Set the following mandatory properties in the `./successmetrics-[releasenumber]/application.properties` file:
```
iq.sm.csvfile = true
iq.url = your IQ server's IP address or URL:8070
iq.user = username of account with access to data in scope
iq.pwd = password of account with access to data in scope
iq.sm.period = month or week (month is recommended for large time periods)
iq.api.payload.timeperiod.first = yyyy-mm or yyyy-ww (eg. 2021-01 for the first month or week of 2021)
```
The following properties are optional:
```
iq.api.payload.timeperiod.last
iq.api.payload.organisation.name
iq.api.payload.application.name
```
To run the app, in the working directory

```
Windows: runapp-docker.bat
Linux: sh runapp-docker.sh
```


# Development

Should you wish to edit the source code:

  * We strongly recommend the use of git flow http://danielkummer.github.io/git-flow-cheatsheet/ to make and manage any changes
  * Clone the repository
  * Branch
  * Make your changes
  * Build and Test
  * Create a PR to merge changes back to the main

```
To test:
./gradlew bootRun

To build:
gradle clean build

To run:
java -jar success-metrics-<version>.jar

To scan with Nexus IQ:
 - first of all, edit the build.gradle file to configure your Nexus IQ url/username as well as your application name, then add the task to the build command above, or run on its own as below:

gradle nexusIQScan

To make a release (using Githib CLI):
gh release create [releasenumber]
```

![image](https://user-images.githubusercontent.com/35227270/141003665-fb2fc00e-6784-4e56-af6f-6c75e2d9d397.png)

# The Fine Print
For large datasets we recommend running extracts for small periods of time and for sets of organisations instead of the full system.

This application is NOT SUPPORTED by Sonatype, and is a contribution of ours to the open source community (read: you!)

Don't worry, using this community item does not "void your warranty". In a worst case scenario, you may be asked by the Sonatype Support team to remove the community item in order to determine the root cause of any issues.

Please remember:

* Use this contribution at the risk tolerance that you have
* Do NOT file Sonatype support tickets related to iq-success-metrics
* DO file issues here on GitHub, so that the community can pitch in
* Phew, that was easier than I thought. Last but not least of all, have fun creating and using this application and the Nexus platform, we are glad to have you here!
