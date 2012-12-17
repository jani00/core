@ECHO OFF
SET nl=^& echo.
IF "%1"=="" (
	GOTO USAGE
)
IF "%1"=="--help" (
	GOTO USAGE
)

java -jar hash-password.jar %1
GOTO :EOF

:USAGE
ECHO Usage: hash-password PASSWORD%nl%Prints the hash code of the given password.
