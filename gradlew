#!/usr/bin/env sh
#
# Gradle startup script for POSIX systems
#

APP_HOME=$( cd "${0%/*}" && pwd -P )
DEFAULT_JVM_OPTS=""
JAVA_CMD="java"

exec "$JAVA_CMD" $DEFAULT_JVM_OPTS -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
