@echo off
title Tunisia Airways Application

cls
echo ===================================
echo    Tunisia Airways Application
echo ===================================
echo.
echo Cleaning previous compilation...
del /s /q *.class >nul 2>&1

echo Compiling Tunisia Airways application...
javac -cp ".;lib/mysql-connector-java-8.0.28.jar;lib/jcalendar-1.4.jar" Main.java LoginPage.java MainMenuPage.java DomesticFlight.java AdminDashboard.java User.java UserDAO.java AppStyle.java TimeSpinner.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Press any key to exit...
    pause > nul
    exit /b 1
)

echo.
echo Running Tunisia Airways application...
echo.
java -cp ".;lib/mysql-connector-java-8.0.28.jar;lib/jcalendar-1.4.jar" Main

echo.
echo Application closed.
echo.
echo Cleaning up compiled files...
echo.
for %%F in (*.class) do (
    echo Removing: %%F
    del "%%F" >nul 2>&1
)
echo.
echo Cleanup complete!

echo.
echo Press any key to exit...
pause > nul 