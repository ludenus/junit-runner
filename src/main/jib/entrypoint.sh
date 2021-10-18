#!/bin/sh

exec java $JVM_OPTS -cp /app/resources:/app/classes:/app/libs/* com.github.ludenus.qa.runner.MainApplication $@
