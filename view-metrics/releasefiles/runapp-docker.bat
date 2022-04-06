@echo off
docker run -p 4040:4040 -v %CD%/config:/config -v %CD%/../iqmetrics:/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics-view-metrics:@APPVER@ %*
