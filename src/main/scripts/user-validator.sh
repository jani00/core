#!/bin/bash

HELP_TEXT="Usage: user-validator USER_DIRECTORY \nValidates all the user files in a directory, according to the specification";
WRAPPED_COMMAND='java -jar user-validator.jar';

if [[ -z "$1" || ($# < 1) || ("$1" == "--help") ]] ; then
	echo -e $HELP_TEXT
else
	COMMAND="$WRAPPED_COMMAND $1";
	$COMMAND
fi
	