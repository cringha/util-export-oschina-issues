@echo on
set MAVEN_HOME=e:\dev\maven
SET JAVA_HOME=E:\Program Files\Java\jdk1.7.0_45
SET PATH=%PATH%;e:\dev\maven\bin
SET MVNOPT=-o -Dmaven.test.skip=true
CALL mvn %MVNOPT% package
CALL mvn  %MVNOPT% dependency:copy-dependencies


SET DEST=dist
SET SRCCONF=.\conf
SET LIBPATH=lib
SET SRCLIBBASE=.\target
SET SRCLIB=%SRCLIBBASE%\dependency
SET OPT=/d /F /y
SET BINSCRIPT=.\bin-scripts

SET CONF=conf
SET APPNAME=util-download-os-issues

REM MKDIR %DEST%\%CONF%
MKDIR %DEST%\%LIBPATH%

 
DEL /F %DEST%\%LIBPATH%\%APPNAME%*.jar 
XCOPY %SRCLIBBASE%\%APPNAME%*.jar  %DEST%\%LIBPATH%\  %OPT%

XCOPY log4j.properties  %DEST%\   %OPT%
XCOPY %SRCLIB%\*.jar  %DEST%\%LIBPATH% %OPT%
XCOPY %BINSCRIPT%\*.*  %DEST%\ %OPT%