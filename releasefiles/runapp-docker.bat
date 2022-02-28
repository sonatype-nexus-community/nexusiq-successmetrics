@echo off

REM full pathname to directory containing metrics csv files
set metricsdir=%1%

set workdir=%cd%

docker run -p 4040:4040 -v ${workdir}:/config -v %metricsdir%:/iqmetrics ghcr.io/sonatype-nexus-community/nexusiq-successmetrics:@APPVER@
