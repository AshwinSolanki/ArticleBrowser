#!/bin/sh
DIR="$( cd "$( dirname "$0" )" && pwd )"
java -Xmx64m -Xms64m -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
