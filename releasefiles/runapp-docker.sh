#!/bin/sh

# full pathname to directory containing metrics csv files
metricsdir=${1}

workdir=$(pwd)

docker run -p 4040:4040 -v "${workdir}":/config -v "${metricsdir}":/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics:@APPVER@
