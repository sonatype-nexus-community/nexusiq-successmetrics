
## Getting Started

### Prerequisites
  * Java 8+
  * (optional) python3  

### Download the java app for success metrics
  * There are two ways to run this application, by directly running the application jar file (Option A) or with the application docker image (Option B)
  * In both cases, start by downloading the zip file. To do so, go to the Releases pane on the right side of this page and click on the latest release
  * Click on the *successmetrics-[releasenumber].zip* file on the assets page to download it
  * Unzip the contents into a directory of your choice 
  * Change into the *successmetrics-[releasenumber]* directory (this will be your working directory)

```
unzip successmetrics-[releasenumber].zip
cd successmetrics-[releasenumber]
```

### Setting runtime properties *application.properties*

There are a number of properties that can be set to control how the application will run. These properties are set in a file called *application.properties*. The file is located in the *config* directory of the working directory. A full description of each property is provided in the file. 

If you are running the application jar file directly (Option A), it is not required to change any properties to run the application. Without any changes, the application will run in web mode in the current directory (ie. the directory in which the zip file is extracted) as the working directory.

The most important default settings are as follows:

####  spring.profiles.active
default: web - other values: pdf, insights

#### data.dir
default: '.' (ie. current directory)

If you are running with the docker image (Option B), there are some mandatory property settings required (see below)

### OPTION A - Run the app (Jar file)

There is one step required before running the application. This is to create the metrics data file the app will use to report on (see Creating the Metrics file). Once this file has been created, the application is ready to run.

The application is already configured with default settings. With the defaults, it will run in web mode.

To run the app, in the working directory

```
Windows: runapp.bat 
Linux: sh runapp.bat
```

### OPTION B - Run the app (Docker)

There is one step required before running the application. This is to set a few runtime properties in the properties file. Once the properties are set, the application is ready to run.

Set the following mandatory properties in the *application.properties* file

* iq.sm.csvfile=true
* iq.url = your IQ server's IP address or URL:8070
* iq.user = username of account with access to data in scope 
* iq.pwd = password of account with access to data in scope
* iq.sm.period = month or week (strongly recommended month)
* iq.api.payload.timeperiod.first = 2020-01 (for example, or 2020-W01 for week format)

The following properties are optional:

* iq.api.payload.timeperiod.last
* iq.api.payload.organisation.name
* iq.api.payload.application.name

To run the app, in the working directory

```
Windows: runapp-docker.bat 
Linux: sh runapp-docker.sh
```

### Run modes

#### web

This app is a simple web app running by default on port 4040. It will start up and you view all the reports and charts via your web browser. By default, the app looks for the *successmetrics.csv* file in the working directory

You only need to keep the app running long enough to view the reports and optionally print them to PDF. This mode will also load the optional data files in the *reports2* directory if available.

The data file(s) are loaded on start-up of the app. Larger files may take a few mins.

On completion, you should see output similar to below after which app is ready for access

```
2020-10-19 20:49:01.271  INFO 93369 --- [  restartedMain] o.s.cs.metrics.service.FileService       : Data loaded.
2020-10-19 20:49:01.271  INFO 93369 --- [  restartedMain] o.s.cs.metrics.runner.StartupRunner      : Ready for browsing at http://localhost:4040
```

Open a browser and go to http://localhost:4040

The *Summary Report* on the web app main page menu is designed to be saved to pdf. It contains most of the other reports. The recommended way to do to this is by selecting the *Save to PDF* option within the Print menu option of your web browser.

#### pdf

You may wish to just simply create a pdf file containing the summary metrics report. A pdf report file is created in a sub-directory of the working directory with a time-stamped file name. The application will then immediately exit after creating the pdf file. 

#### insights

In this mode, the application will simply create a CSV file containing the data required in order to create an Insights Analysis report. The CSV  file is created in a sub-directory in the working directory with a time-stamped file name. The application will then immediately exit after creating the file. 

### Creating the Metrics file

The metrics file is called *successmetrics.csv*. We recommend manually creating the CSV file, particularly where the report will cover ALL organisations and applications.

#### (Optional) Make Config Updates for Success Metrics

Creating the metrics file, requires sending a payload to the Nexus IQ success metrics API. Two files are provided as below. We recommend reporting on a monthly basis making use of the *monthly.json* file.

 * Edit either the included *monthly.json* or *weekly.json* file to adjust the firstTimePeriod (the week or month to start reporting from) and optionally add an end period
 ```
 Example: the following request body will fetch data for all organisations and applications between Jan 2020 and Sept 2021 on a monthly basis:
 {
  "timePeriod": "MONTH",
  "firstTimePeriod": "2020-01",
  "lastTimePeriod": "2021-09",
  "applicationIds": [],
  "organizationIds": []
 }
 ```
 * Additional information can be found here: https://help.sonatype.com/iqserver/automating/rest-apis/success-metrics-data-rest-api---v2.

 * For larger installations, we recommend limiting the data to extract to a smaller period (e.g. since the previous month or few weeks) or subset of organisations and/or applications. Information on how to adjust the request body (in the weekly.json or monthly.json files) can be found at the success metrics page above.

 * Please note that for any case, a minimum of three (3) data points are needed to produce the graphs, so please adjust the *monthly.json* or *weekly.json* to account for this. Please also note that by default, only fully completed months (or weeks) are considered, so in the example above, the script will collect monthly data from January 2020 until September 2021, generating 21 data points if September 2021 was fully completed (current date is October 2021 onwards) or just 20 data points if not.

#### Create the metrics file

*There is a script available to help creating the required CSV file*
 
 * Open a command prompt and run 

```
Windows: create-data.bat <iq-host-url> <iq-username> <iq-password> <period-file>
Linux: create-data.sh <iq-host-url> <iq-username> <iq-password> <period-file>

iq-host-url - your Nexus IQ Url, (with no backslash at the end - it will not work with a trailing forward slash)
iq-username - your Nexus IQ user name (choose a user name that has access to data set you'd like to report on)
iq-password - your Nexus IQ password
period-file - weekly.json or monthly.json

Example (Windows):  create-data.bat http://localhost:8070 admin admin123 monthly.json
```

The script will create a file called *successmetrics.csv*. We suggest opening the file and checking it to ensure it contains metrics data.

#### (Optional) Additional Reports

If you have python3 available, you can run the following script to produce additonal data files for reporting, all of which will be read by the app on startup.
This script is optional and not required for the main success metrics report
```
cd reports2
Windows: create-data.bat <iq-host-url> <iq-username> <iq-password>
Linux: create-data.sh <iq-host-url> <iq-username> <iq-password>
cd ..

iq-host-url - your Nexus IQ Url, (with no backslash at the end - it will not work with a trailing forward slash)
iq-username - your Nexus IQ user name (choose a user name that has access to the data set you'd like to report on)
iq-password - your Nexus IQ password

Example (Windows):  create-data.bat http://localhost:8070 admin admin123
```

These additional files include a list of policy violations, applications scanned last date, a list of components in quarantine (nxrm3) and a list of waivers.

The files are created in the reports2 directory

(Make sure to return to the working directory to run the app).

## Development

Should you wish to edit the source code: 

  * We strongly recommend the use of git flow http://danielkummer.github.io/git-flow-cheatsheet/ to make and manage any changes
  * Clone the repository
  * Make your changes
  * At the command line in the root directory of the repo

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

## The Fine Print
* We recommend running it for 4 weeks of data at a time and for sets of orgs instead of the full scope if you have a large dataset.
* It is worth noting that this is NOT SUPPORTED by Sonatype, and is a contribution of ours to the open source community (read: you!)

* Don't worry, using this community item does not "void your warranty". In a worst case scenario, you may be asked by the Sonatype Support team to remove the community item in order to determine the root cause of any issues.

* Remember:

* Use this contribution at the risk tolerance that you have
* Do NOT file Sonatype support tickets related to iq-success-metrics
* DO file issues here on GitHub, so that the community can pitch in
* Phew, that was easier than I thought. Last but not least of all:

* Have fun creating and using this application and the Nexus platform, we are glad to have you here!
