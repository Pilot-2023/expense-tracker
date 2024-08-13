FROM 19-jdk-alpine3.16

ENV SERVICE_NAME=expense-tracker \
    GROUP_NAME=expense-tracker \
    GID=30020 \
    UID=30020

# setup group and user, initialize home
RUN addgroup --gid ${GID} ${GROUP_NAME} && \
    adduser --home /opt/app --system --shell /sbin/nologin --uid ${UID} ${GROUP_NAME}

COPY target/*.jar /opt/app/${SERVICE_NAME}.jar

RUN chown -R ${GROUP_NAME} /opt/app

USER $GROUP_NAME

WORKDIR /opt/app

ENTRYPOINT exec java $JAVA_OPTS -jar ./${SERVICE_NAME}.jar