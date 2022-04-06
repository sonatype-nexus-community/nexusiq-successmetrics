#!/bin/sh
docker run -p 4040:4040 -v "$PWD"/config:/config -v "$PWD"/../iqmetrics/:/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics-view-metrics:@APPVER@ "$@"
