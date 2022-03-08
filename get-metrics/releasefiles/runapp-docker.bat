@echo off
docker run --rm -it -v %CD%/config:/config -v %CD%/../iqmetrics:/iqmetrics ghcr.io/sonatype-nexus-community/getmetrics:@APPVER@
