@ECHO OFF
SET nl=^& echo.
IF "%1"=="" (
	GOTO USAGE
)
IF "%1"=="--help" (
	GOTO USAGE
) 

java -jar user-validator.jar %1
GOTO :EOF

:USAGE
ECHO Usage: user-validator USER_DIRECTORY%nl%Validates all the user files in a directory, according to the specification