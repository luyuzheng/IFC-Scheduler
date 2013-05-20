ECHO OFF
TITLE My Test Batch File

SET serverIP=192.168.0.12
SET ip_address_string="IPv4 Address"
REM "May need to use 'IP Address' above for Windows XP. Better double check."

SETLOCAL EnableDelayedExpansion
FOR /f "usebackq tokens=2 delims=:" %%i IN (`ipconfig ^| FINDSTR /c:%ip_address_string%`) DO (
	IF "%serverIP%"=="%%i" (
		GOTO SERVERCOMPUTER
	) ELSE (
		GOTO CLIENTCOMPUTER
	)
)

:SERVERCOMPUTER
REM "Is it necessary to check if MySQL is running and start it if XAMPP automatically starts when computer boots up?"
TASKLIST /FI "IMAGENAME eq mysqld.exe" | FIND /I /N "mysqld.exe">NUL
IF ERRORLEVEL 0 (
	GOTO STARTSCHEDULER
) ELSE (
	GOTO STARTALL
)

:CLIENTCOMPUTER
ECHO "Checking if we can connect to the server..."
PING -n 1 %serverIP% > NUL
IF ERRORLEVEL 0 (
	ECHO "The computer is on... so XAMPP should have been started... maybe."
) ELSE (
	ECHO "Valarie's computer needs to be turned on before the scheduler can be used. Please turn it on and then try running this script again."
)
GOTO END

:STARTALL
START C:\xampp\xampp_start.exe
ECHO "Starting up the server..."
PAUSE
IF ERRORLEVEL 0 (
	GOTO STARTSCHEDULER
) ELSE (
	ECHO "Server failed to start."
	GOTO END
)

:STARTSCHEDULER
START C:\Users\Jean\Documents\GitHub\IFC-Scheduler\IFC_update5.jar
ECHO "Starting up the scheduler, please wait a few seconds for it to load."
GOTO END

:END
PAUSE
