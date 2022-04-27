#!/bin/sh

docker run --rm -it -v "$PWD"/config:/config -v "$PWD"/../iqmetrics:/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics-get-metrics:@APPVER@ "$@"
