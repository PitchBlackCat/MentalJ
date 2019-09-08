#!/bin/bash

echo "-= BUILDING =-";
echo;
mvn package;
echo;
echo "-= Running =-";
echo;
mvn -q exec:java
