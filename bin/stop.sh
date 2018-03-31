#!/bin/bash

ps ax | grep -i 'demo' | grep java | awk '{print $1}' | xargs kill

echo
echo "demo should be killed"
echo