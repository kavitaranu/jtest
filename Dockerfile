FROM python:3.10-slim-buster
LABEL maintainer="core.platform@nexmo.com"

WORKDIR /app
COPY . /app
RUN pip install
CMD ["python3", "-m", "app.api_call_token"]