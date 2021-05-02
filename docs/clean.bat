REM Mr. A. Maganlal
REM Computer Science 2A 2021
REM This batch file cleans a project for submission

REM Turn echo off and clear the screen
@echo off
cls

REM Good batch file coding practice
setlocal enabledelayedexpansion

REM Paths for Java
echo Change JAVA_HOME path
SET JAVA_HOME="C:\Program Files (x86)\jdk-15.0.2"
SET PATH=%PATH%;%JAVA_HOME%

REM Move to correct folder
echo Build script set to run in Project folder
cd ..

REM Variables for batch
set ERRMSG=
set PRAC_BIN=.\bin
set PRAC_DOCS=.\docs
set PRAC_LIB=.\lib\*
set PRAC_SRC=.\src

REM Clean all class files from bin folder and remove JavaDoc
:CLEAN
echo ~~~ Cleaning project ~~~
DEL /S %PRAC_BIN%\*.class
@ECHO ON
RMDIR /Q /S %PRAC_DOCS%\JavaDoc
@ECHO OFF
IF /I "%ERRORLEVEL%" NEQ "0" (
    echo !!! Error cleaning project !!!
)

REM Something went wrong, display error
:ERROR
echo !!! Fatal error with project !!!
echo %ERRMSG%

REM Move back to docs folder and wait
:END
echo ~~~ End ~~~
cd %PRAC_DOCS%
pause