#! /bin/bash
set -e

exec java \
          $APPLICATION_HEAP_OPTS \
          $APPLICATION_JAVA_OPTS \
          -jar "$APPLICATION_HOME/app.jar" \
          $*
