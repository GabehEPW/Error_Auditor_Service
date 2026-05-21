@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script, Windows
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set MAVEN_WRAPPER_DIR=%~dp0.mvn\wrapper
set MAVEN_WRAPPER_JAR=%MAVEN_WRAPPER_DIR%\maven-wrapper.jar
set MAVEN_PROJECT_DIR=%~dp0
if "%MAVEN_PROJECT_DIR:~-1%"=="\" set MAVEN_PROJECT_DIR=%MAVEN_PROJECT_DIR:~0,-1%

if not exist "%MAVEN_WRAPPER_JAR%" (
  if not exist "%MAVEN_WRAPPER_DIR%" mkdir "%MAVEN_WRAPPER_DIR%"
  echo Downloading Maven Wrapper...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -UseBasicParsing -Uri https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar -OutFile \"%MAVEN_WRAPPER_JAR%\" }"
)

java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECT_DIR%" -cp "%MAVEN_WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
if errorlevel 1 exit /b 1

endlocal
