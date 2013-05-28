ECHO OFF
TITLE My Test Batch File

SET serverIP= 192.168.118.171
SET ip_address_string="IPv4 Address"
REM "May need to use 'IP Address' above for Windows XP. Better double check."

SETLOCAL EnableDelayedExpansion
FOR /f "usebackq tokens=2 delims=:" %%i IN (`ipconfig ^| FINDSTR /c:%ip_address_string%`) DO SET ip=%%i
IF "%serverIP%"=="%ip%" (
	GOTO SERVERCOMPUTER
) ELSE (
	GOTO CLIENTCOMPUTER
)

:SERVERCOMPUTER
REM This should not be necessary if XAMPP is automatically started when the computer is booted up, but just in case...
TASKLIST /FI "IMAGENAME eq mysqld.exe" | FIND /I /N "mysqld.exe">NUL
IF "%ERRORLEVEL%"=="0" (
	GOTO STARTSCHEDULER
) ELSE (
	GOTO STARTALL
)

:CLIENTCOMPUTER
ECHO Checking if we can connect to the server computer...
ECHO.
PING -n 1 %serverIP% > NUL
IF "%ERRORLEVEL%"=="0" (
	ECHO Success, connected to the server computer.
	GOTO STARTSCHEDULER
) ELSE (
	ECHO The computer with the XAMPP server needs to be turned on before the scheduler 
	ECHO can be used. Please turn it on and then try running this script again.
	ECHO.
	PAUSE
)
GOTO END

:STARTALL
START C:\xampp\xampp_start.exe
ECHO Starting up the server...
PAUSE
IF "%ERRORLEVEL%"=="0" (
	ECHO Server started.
	GOTO STARTSCHEDULER
) ELSE (
	ECHO Server failed to start.
	REM The application will display its own server error message, so the batch file simply exits.
	GOTO END
)

:STARTSCHEDULER
IF EXIST IFC_Scheduler.jar (
	START IFC_Scheduler.jar
	ECHO Starting up the scheduler, please wait a few seconds for it to load.
	TIMEOUT 5
) ELSE (
	ECHO The scheduler application could not be found.
	ECHO Please make sure that the application is in the folder where this script is.
	ECHO.
	PAUSE
)
GOTO END

:END
