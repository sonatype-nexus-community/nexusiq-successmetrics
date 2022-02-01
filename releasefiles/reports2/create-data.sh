#!/bin/sh

iqurl="${1}"
iquser="${2}"
iqpwd="${3}"

python3 reports2.py "${iqurl}" "${iquser}" "${iqpwd}"
