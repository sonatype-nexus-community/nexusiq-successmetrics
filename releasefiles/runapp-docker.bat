@echo off

set workdir=%cd%
set metricsdir=%1%

docker run -p 4040:4040 -v ${workdir}:/config -v %metricsdir%:/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics:@APPVER@
