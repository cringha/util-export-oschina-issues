@echo off
REM SET JAVA_HOME=D:\Program Files\java\jdk1.5.0_09
SET JAVA="%JAVA_HOME%\bin\java.exe"
set LIBRARY=.\lib
SET JAVAOPT=

set CLASSPATH=

@echo off
FOR /R %LIBRARY% %%a in (*.jar) DO CALL :AddToPath %%a
GOTO :RUN

:AddToPath
SET CLASSPATH=%1;%CLASSPATH%
GOTO :EOF
 

:RUN  
rem ECHO %CLASSPATH%


%JAVA% %JAVAOPT% -cp  "%CLASSPATH%" com.siemens.ct.its.util.ExportIssues %*