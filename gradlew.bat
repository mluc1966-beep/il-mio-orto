@echo off
setlocal
set "GRADLE_VERSION=8.13"
if "%GRADLE_USER_HOME%"=="" set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "BASE_DIR=%GRADLE_USER_HOME%\wrapper\dists\gradle-%GRADLE_VERSION%-bin\manual"
set "ZIP_FILE=%BASE_DIR%\gradle-%GRADLE_VERSION%-bin.zip"
set "GRADLE_HOME=%BASE_DIR%\gradle-%GRADLE_VERSION%"
set "URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip"

if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  if not exist "%BASE_DIR%" mkdir "%BASE_DIR%"
  if not exist "%ZIP_FILE%" (
    echo Download di Gradle %GRADLE_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%URL%' -OutFile '%ZIP_FILE%'"
    if errorlevel 1 exit /b 1
  )
  if exist "%GRADLE_HOME%" rmdir /s /q "%GRADLE_HOME%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ZIP_FILE%' -DestinationPath '%BASE_DIR%' -Force"
  if errorlevel 1 exit /b 1
)

call "%GRADLE_HOME%\bin\gradle.bat" %*
endlocal
