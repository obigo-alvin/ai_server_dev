#!/bin/bash
set +o history
read -p "Enter The OPENAI_KEY : " OPENAI_API_KEY
read -p "Enter The GOOGLE_KEY : " GOOGLE_VISION_KEY
echo "OPENAI_KEY value : $OPENAI_API_KEY"
echo "GOOGLE_KEY value : $GOOGLE_VISION_KEY"

export OPENAI_API_KEY
export GOOGLE_VISION_KEY
set -o history

if [ -z "$OPENAI_API_KEY" ]; then
    echo "OPENAI KEY is empty"
    # abnormal exit
    exit 1
fi

if [ -z "$GOOGLE_VISION_KEY" ]; then
    echo "GOOGLE KEY is empty"
    exit 1
fi

PID=ps -ef | grep java | grep -w "obigo_ai_server-0.0.1-SNAPSHOT.jar" | awk '{ print $2 }'
if [ -n "$PID" ]; then
  kill -9 $PID
fi

./jdk-21/bin/java -Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true -jar obigo_ai_server-0.0.1-SNAPSHOT.jar > obigo_ai_server.log 2>&1 &

unset OPENAI_API_KEY
unset GOOGLE_VISION_KEY
