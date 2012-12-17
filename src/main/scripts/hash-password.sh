#!/bin/bash

HELP_TEXT="Usage: hash-password PASSWORD \nPrints the hash code of the given password.";
WRAPPED_COMMAND='java -jar hash-password.jar';

if [[ -z "$1" || ($# < 1) || ("$1" == "--help") ]] ; then
	echo -e $HELP_TEXT
else
	COMMAND="$WRAPPED_COMMAND $1";
	$COMMAND
fi
	