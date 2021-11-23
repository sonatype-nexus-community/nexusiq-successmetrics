#!/bin/sh

iqurl=${1}
iquser=${2}
iqpwd=${3}
payloadfile=${4}

outputfile=successmetrics.csv

curl -k -u ${iquser}:${iqpwd} -X POST -H "Accept: text/csv" -H "Content-Type: application/json" -o ${outputfile} -d@${payloadfile} ${iqurl}/api/v2/reports/metrics 

echo ${outputfile}

