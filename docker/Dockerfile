FROM openjdk:11-jre-slim

WORKDIR /opt/jopiter-backend
ENTRYPOINT ["/opt/jopiter-backend/entrypoint.sh"]
ENV APPLICATION_HOME=/opt/jopiter-backend

ARG VERSION
ARG JAR_PATH
ENV APP_VERSION=${VERSION}

ADD resources /opt/jopiter-backend
ADD ${JAR_PATH} /opt/jopiter-backend/app.jar