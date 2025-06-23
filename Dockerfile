//FROM 564623767830.dkr.ecr.eu-west-1.amazonaws.com/nexmo-java:8-corretto-jdk-minideb
FROM python:3.10-slim-buster
LABEL maintainer="core.platform@nexmo.com"

ARG service_name
ARG service_user=${service_name}
ARG service_uid=1000
ARG service_group=${service_user}
ARG service_gid=1000
ARG local_deb_path
ARG jwt_group=sip-1
ARG jwt_gid=1039

ENV service_name=${service_name}
ENV service_user=${service_user}
ENV service_uid=${service_uid}
ENV service_group=${service_group}
ENV service_gid=${service_gid}
ENV jwt_group=${jwt_group}
ENV jwt_gid=${jwt_gid}
ENV DAEMON_RUNTIME=/home/${service_user}/runtime
ENV bootlog=${DAEMON_RUNTIME}/boot.log
ENV logfile=${DAEMON_RUNTIME}/logs/nexmo-${service_name}.log
ENV pidfile=/var/run/nexmo/nexmo-${service_name}.pid
WORKDIR /app
COPY . /app
RUN pip install -r requirements.txt
CMD ["python3", "-m", "app.api_call_token"]