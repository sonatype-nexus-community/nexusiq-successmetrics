@echo off

set iqurl=%1
set iquser=%2
set iqpwd=%3

python3 reports2.py %iqurl% %iquser% %iqpwd%
