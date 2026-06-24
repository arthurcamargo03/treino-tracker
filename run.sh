#!/bin/bash
set -e

mvn -q compile
java -cp target/classes com.treinotracker.Main
