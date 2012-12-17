#!/bin/bash

HELP_TEXT="Usage: resource-validator OPTION RESOURCE_DIRECTORY \nValidates the resource structure, according to the option passed.\n
Available options are:\n
-p			Validate a problem directory\n
-c			Validate a contest directory\n
-s 			Validate a series directory\n";
WRAPPED_COMMAND='java -jar resource-validator.jar';

if [[ -z "$1" || ($# != 2) || ("$1" == "--help") ]] ; then
	echo -e $HELP_TEXT
else
	COMMAND="$WRAPPED_COMMAND $1 $2";
	$COMMAND
fi
	