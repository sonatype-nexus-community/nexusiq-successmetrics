@echo off

set workdir=%cd%
set datadir=%workdir%

docker run -p 4040:4040 -v ${workdir}:/config -v %workdir%:%datadir% -e data.dir=%datadir% ghcr.io/sonatype-nexus-community/iqsuccessmetrics2:@APPVER@


