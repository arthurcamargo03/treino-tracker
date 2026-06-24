@echo off
chcp 65001 >nul
setlocal

call mvn -q compile
java -cp target\classes com.treinotracker.Main

endlocal
