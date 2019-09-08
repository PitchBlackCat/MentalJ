#!/bin/bash
set -e

echo;
args=`echo "\"-Dexec.args=$@"`
./mental.sh racer exec:java -q ${args};
