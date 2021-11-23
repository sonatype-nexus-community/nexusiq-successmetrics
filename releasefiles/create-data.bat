@echo off

set iqurl=%1
set iquser=%2
set iqpwd=%3
set payloadfile=%4

set outputfile=successmetrics.csv

curl -k -u %iquser%:%iqpwd% -X POST -H "Accept: text/csv" -H "Content-Type: application/json" -o %outputfile% -d@%payloadfile% %iqurl%/api/v2/reports/metrics

echo %outputfile%
