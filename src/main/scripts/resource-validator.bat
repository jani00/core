@ECHO OFF
SET nl=^& echo.
IF "%1"=="" (
	GOTO USAGE
)
IF "%1"=="--help" (
	GOTO USAGE
)
IF "%2"=="" (
	GOTO USAGE
)
java -jar resource-validator.jar %1 %2
GOTO :EOF

:USAGE
ECHO Usage: resource-validator OPTION RESOURCE_DIRECTORY%nl%Validates the resource structure, according to the option passed.%nl%Available options are:%nl%-p			Validate a problem directory%nl%-c			Validate a contest directory%nl%-s 			Validate a series directory%nl%