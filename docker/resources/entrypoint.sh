#! /bin/bash
set -e

JMX_OPTS="${JMX_OPTS:--XX:+UnlockCommercialFeatures -XX:+FlightRecorder -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.port=9090}"

exec java \
          $APPLICATION_HEAP_OPTS \
          $APPLICATION_JMX_OPTS \
          $APPLICATION_JAVA_OPTS \
          -jar "$APPLICATION_HOME/app.jar" \
          $*