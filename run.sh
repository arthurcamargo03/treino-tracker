#!/bin/bash
set -e

javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.treinotracker.Main
