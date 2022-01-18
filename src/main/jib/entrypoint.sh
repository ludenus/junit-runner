#!/bin/sh

set -x
java $JVM_OPTS -javaagent:/app/resources/aspectjweaver-1.9.5.jar -cp /app/resources:/app/classes:/app/libs/* qa.runner.MainApplication $@
set +x

export KEEP_POD_RUNNING_AFTER_TEST_ARE_DONE=${KEEP_POD_RUNNING_AFTER_TEST_ARE_DONE:-"true"}

if [ "${KEEP_POD_RUNNING_AFTER_TEST_ARE_DONE}" = "true"  ]; then
  while true; do
    echo "`date '+%Y-%m-%d %H:%M:%S %Z'`: KEEP_POD_RUNNING_AFTER_TEST_ARE_DONE=${KEEP_POD_RUNNING_AFTER_TEST_ARE_DONE} keeping pod running after tests are done..."
    sleep 10
  done
fi

if [ -n "${DEBUG_SLEEP_BEFORE_EXIT_SECONDS}" ]; then
  echo "debug sleep ${DEBUG_SLEEP_BEFORE_EXIT_SECONDS} seconds before exit..."
  sleep ${DEBUG_SLEEP_BEFORE_EXIT_SECONDS}
fi