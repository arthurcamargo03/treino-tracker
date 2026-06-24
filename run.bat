@echo off
chcp 65001 >nul
setlocal

if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
del sources.txt
java -cp out com.treinotracker.Main

endlocal
